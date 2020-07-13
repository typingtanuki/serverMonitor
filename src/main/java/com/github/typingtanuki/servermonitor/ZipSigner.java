package com.github.typingtanuki.servermonitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class ZipSigner {

   public static void sign(Path toSign, Path certificate)
         throws IOException {
      Signature signature = getOrMakeSignature();

      try {
         byte[] bytes = Files.readAllBytes(toSign);
         signature.update(bytes);
         byte[] digitalSignature = signature.sign();

         Files.write(certificate, digitalSignature);
      } catch (SignatureException e) {
         throw new IOException("Could not sign file", e);
      }
   }

   public static void checkSign(Path toCheck, Path certificate)
         throws IOException {
      PublicKey publicKey = savedPublicKey();

      try {
         Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
         signature.initVerify(publicKey);

         byte[] bytes = Files.readAllBytes(toCheck);
         signature.update(bytes);

         byte[] digitalSignature = Files.readAllBytes(certificate);
         if (!signature.verify(digitalSignature)) {
            throw new IOException("Invalid signature");
         }
      } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException e) {
         throw new IOException("Could not check certificate", e);
      }
   }


   private static PublicKey savedPublicKey()
         throws IOException {
      Path path = Paths.get("./conf/key.pub");
      if (!Files.exists(path)) {
         throw new IOException("Missing public key: " + path);
      }

      try {
         byte[] bytes = Files.readAllBytes(path);
         KeyFactory kf = KeyFactory.getInstance("DSA", "SUN");
         return kf.generatePublic(new PKCS8EncodedKeySpec(bytes));
      } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException e) {
         throw new IOException("Could not load private key", e);
      }
   }

   private static Signature getOrMakeSignature()
         throws IOException {
      try {
         PrivateKey privateKey = savedPrivateKey();
         if (privateKey == null) {
            privateKey = makeNewKeys();
         }

         // Get an instance of Signature object and initialize it.
         Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
         signature.initSign(privateKey);

         return signature;
      } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException e) {
         throw new IOException("Could not build signature", e);
      }
   }

   private static PrivateKey savedPrivateKey() throws IOException {
      Path path = Paths.get("./conf/key.pri");
      if (!Files.exists(path)) {
         return null;
      }

      try {
         byte[] bytes = Files.readAllBytes(path);
         KeyFactory kf = KeyFactory.getInstance("DSA", "SUN");
         return kf.generatePrivate(new PKCS8EncodedKeySpec(bytes));
      } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException e) {
         throw new IOException("Could not load private key", e);
      }
   }

   private static PrivateKey makeNewKeys() throws IOException {
      try {
         // Get instance and initialize a KeyPairGenerator object.
         KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
         SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
         keyGen.initialize(1024, random);

         // Get a PrivateKey from the generated key pair.
         KeyPair keyPair = keyGen.generateKeyPair();
         PrivateKey privateKey = keyPair.getPrivate();
         PublicKey publicKey = keyPair.getPublic();

         saveKey("pri", privateKey.getEncoded());
         saveKey("pub", publicKey.getEncoded());

         return privateKey;
      } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
         throw new IOException("Could not generate new keys", e);
      }
   }

   private static void saveKey(String type, byte[] key) throws IOException {
      Path path = Paths.get("./conf/key." + type);
      Files.write(path, key, StandardOpenOption.CREATE_NEW);
   }
}

package com.github.typingtanuki.servermonitor.web.config;

import com.github.typingtanuki.servermonitor.MonitorMain;
import com.github.typingtanuki.servermonitor.config.MainConfig;
import com.github.typingtanuki.servermonitor.core.ServerMonitor;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * REST endpoint for config
 *
 * @author clerc
 * @since 2020/02/07
 */
@Path("/config")
public class ConfigEndpoint {
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public MainConfig fetchSettings() {
      ServerMonitor monitor = MonitorMain.monitor;
      return monitor.currentConfig();
   }

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public MainConfig updateSettings(@QueryParam("persist") boolean persist,
                                    MainConfig newConfig) throws IOException {
      ServerMonitor monitor = MonitorMain.monitor;
      return monitor.updateConfig(newConfig, persist);
   }

   @POST
   @Path("/updateMonitor")
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   @Produces(MediaType.APPLICATION_JSON)
   public void updateMonitor(
         @FormDataParam("zip") InputStream zipInputStream,
         @FormDataParam("cert") InputStream certInputStream) throws IOException {
      if (zipInputStream == null || certInputStream == null) {
         throw new IOException("No input files");
      }

      java.nio.file.Path uploadedZip = Files.createTempFile("monitorUpdate", ".zip");
      java.nio.file.Path uploadedCert = Files.createTempFile("monitorUpdate", ".cert");

      download(zipInputStream, uploadedZip);
      download(certInputStream, uploadedCert);

      ServerMonitor monitor = MonitorMain.monitor;
      monitor.updateMonitor(uploadedZip, uploadedCert);
   }

   private void download(InputStream inputStream,
                         java.nio.file.Path target) {
      int read = 0;
      byte[] bytes = new byte[1024];

      try (OutputStream out = new FileOutputStream(target.toFile())) {
         while ((read = inputStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
         }
         out.flush();
      } catch (IOException e) {
         throw new WebApplicationException(
               "Error while uploading file. Please try again !!");
      }
   }
}

import {html, TemplateResult} from "lit-element";
import {ServerList} from "./server-list";
import {ServerInfo} from "../rest/types";

export function serverListTemplate(serverList: ServerList): TemplateResult {
    return html`<div class="root">${serverList.servers.map(
        server => formatServer(server))}</div>`;
}

function formatServer(server: ServerInfo): TemplateResult {
    return html`<server-entry .server="${server}"></server-entry>`;
}
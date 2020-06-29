import {CSSResult, customElement, html, LitElement, TemplateResult, unsafeCSS} from 'lit-element';
import listStyle from "./server-list.less";
import {RestClient, ServerInfo} from "../rest/rest-client";
import {ServerEntry} from "../server-entry/server-entry";

@customElement('server-list')
export class ServerList extends LitElement {
    public static get dependencies(): any[] {
        return [ServerEntry];
    }

    public static get styles(): CSSResult {
        return unsafeCSS(listStyle);
    }

    static get properties() {
        return {
            client: {type: Object},
            servers: {type: Array},
            serverIPs: {type: Object}
        };
    }

    public client: RestClient;
    public serverIPs: { [id: string]: string };
    public servers: ServerInfo[] = [];

    constructor() {
        super();
    }

    public render(): TemplateResult {
        return html`${this.servers.map(server => this.formatServer(server))}`;
    }

    public firstUpdated(): void {
        this.refresh();
    }

    public refresh(): void {
        const self: ServerList = this;
        this.client.getServerState(this)
            .then(function (): void {
                const servers: NodeListOf<ServerEntry> = self.shadowRoot.querySelectorAll("server-entry");
                for (let i = 0; i < servers.length; i++) {
                    servers[i].requestUpdate();
                }
            })
            .catch(function (error: string | Error) {
                console.log(error);
            });
    }

    private formatServer(server: ServerInfo): TemplateResult {
        return html`<server-entry .server="${server}"></server-entry>`;
    }

    public resolve(server: ServerInfo, currentServer: string): string {
        if (this.serverIPs.hasOwnProperty(server.name)) {
            return this.serverIPs[server.name];
        }
        return currentServer;
    }
}
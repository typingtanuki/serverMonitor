import {CSSResult, customElement, LitElement, TemplateResult, unsafeCSS} from 'lit-element';

import rootStyle from "./monitor-root.less";
import {RestClient, ServerInfo} from "../rest/rest-client";
import {ServerList} from "../server-list/server-list";
import {ServerEntrySelectedEvent} from "../server-entry/server-entry";
import {DetailView} from "../detail-view/detail-view";
import {monitorRootTemplate} from "./monitor-root-template";

@customElement('monitor-root')
export class MonitorRoot extends LitElement {

    public static get styles(): CSSResult {
        return unsafeCSS(rootStyle);
    }

    static get properties() {
        return {
            showDetails: {type: Boolean},
            rootClient: {type: Object},
            currentClient: {type: Object},
            currentServerInfo: {type: Object}
        };
    }

    public showDetails: boolean;
    public readonly rootClient: RestClient | null;
    public currentClient: RestClient | null;
    public currentServerInfo: ServerInfo;

    constructor() {
        super();

        this.rootClient = new RestClient(this.webServer());
    }

    public static get dependencies(): any[] {
        return [ServerList, DetailView];
    }

    public render(): TemplateResult {
        return monitorRootTemplate(this);
    }

    public firstUpdated(): void {
        const self: MonitorRoot = this;
        this.addEventListener(ServerEntrySelectedEvent.event, function (event: ServerEntrySelectedEvent) {
            self.selectedServer(event.detail.server);
        });
    }

    public refresh(): void {
        const serverList: ServerList | null = this.shadowRoot.querySelector("server-list");
        if (serverList !== null) {
            serverList.refresh();
        }

        const detailsView: DetailView = this.shadowRoot.querySelector(".details.half detail-view");
        if (detailsView !== null) {
            detailsView.changeProperties();
        }
    }

    private webServer(): string {
        return window.location.href.split("/www/")[0];
    }

    private async selectedServer(server: ServerInfo): Promise<void> {
        this.showDetails = true;

        const serverList: ServerList | null = this.shadowRoot.querySelector("server-list");
        this.currentServerInfo = server;
        this.currentClient = new RestClient(serverList.resolve(server, this.webServer()));

        await this.requestUpdate();

        const detailView: DetailView | null = this.shadowRoot.querySelector("detail-view");
        if (detailView !== null) {
            detailView.changeProperties();
        }
    }
}
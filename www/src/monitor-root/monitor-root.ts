import {CSSResult, customElement, LitElement, TemplateResult, unsafeCSS} from 'lit-element';

import rootStyle from "./monitor-root.less";
import {RestClient} from "../rest/rest-client";
import {ServerList} from "../server-list/server-list";
import {ServerEntrySelectedEvent} from "../server-entry/server-entry";
import {DetailView} from "../detail-view/detail-view";
import {monitorRootTemplate} from "./monitor-root-template";
import {ModalRoot} from "../modal-root/modal-root";
import {ServerInfo} from "../rest/types";
import {IconSvg} from "../icon-svg/icon-svg";
import {icon, Icon, iconSpinner} from "../icon-svg/icons";
import Velocity from "velocity-animate";

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

    public iconLoading: Icon = icon(iconSpinner);
    public showDetails: boolean;
    public readonly rootClient: RestClient | null;
    public currentClient: RestClient | null;
    public currentServerInfo: ServerInfo;

    private restChecker: (() => void) | null = null;
    private restCheckerHandle: number;

    constructor() {
        super();

        this.rootClient = new RestClient(this.webServer());
    }

    public static get dependencies(): any[] {
        return [ServerList, DetailView, ModalRoot, IconSvg];
    }

    public render(): TemplateResult {
        return monitorRootTemplate(this);
    }

    public connectedCallback(): void {
        super.connectedCallback();

        if (this.restChecker === null) {
            const self: MonitorRoot = this;
            this.restChecker = function () {
                self.checkRest();
            }
        }
        this.restCheckerHandle = window.setInterval(this.restChecker, 1000);
    }

    public disconnectedCallback(): void {
        super.disconnectedCallback();

        window.clearInterval(this.restCheckerHandle);
    }

    public firstUpdated(): void {
        const self: MonitorRoot = this;
        this.addEventListener(ServerEntrySelectedEvent.event,
            function (event: ServerEntrySelectedEvent) {
                self.selectedServer(event.detail.server);
            });
    }

    public closeDetails(): void {
        const detailsView: DetailView = this.shadowRoot.querySelector(
            ".details.half detail-view");
        detailsView.client = null;
        this.showDetails = false;
    }

    public async refresh(): Promise<void> {
        await this.requestUpdate();
        const serverList: ServerList | null = this.shadowRoot.querySelector("server-list");
        if (serverList !== null) {
            serverList.refresh();
        }

        const detailsView: DetailView = this.shadowRoot.querySelector(
            ".details.half detail-view");
        if (detailsView !== null) {
            detailsView.changeProperties();
        }
    }

    public async showModal(header: string, content: TemplateResult): Promise<void> {
        const modal: ModalRoot = this.shadowRoot.querySelector("modal-root");
        modal.header = header;
        modal.content = content;
        return modal.open();
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
            detailView.settingsMode = false;
            detailView.uploadMode = false;
            detailView.changeProperties();
        }
    }

    private checkRest(): void {
        const spinner: HTMLElement | null = this.shadowRoot.querySelector(".loading");
        if (spinner === null) {
            return;
        }
        if (RestClient.isBusy()) {
            spinner.style.opacity = "1";
        } else {
            spinner.style.opacity = "0";
        }
    }
}
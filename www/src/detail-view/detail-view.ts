import {CSSResult, customElement, html, LitElement, TemplateResult, unsafeCSS} from 'lit-element';
import listStyle from "./detail-view.less";
import {RestClient, ServerInfo} from "../rest/rest-client";
import {Report} from "../report";
import {ReportList} from "../report-list/report-list";
import {detailViewTemplate} from "./detail-view.template";

@customElement('detail-view')
export class DetailView extends LitElement {

    public static get dependencies(): any[] {
        return [ReportList];
    }

    public static get styles(): CSSResult {
        return unsafeCSS(listStyle);
    }

    static get properties() {
        return {
            client: {type: Object},
            server: {type: Object},
            success: {type: Array},
            failure: {type: Array}
        };
    }

    public client: RestClient = null;
    public server: ServerInfo = {name: "", monitors: []};
    public success: Report[] = [];
    public failure: Report[] = [];

    constructor() {
        super();
    }

    public render(): TemplateResult {
        if (!this.server) {
            return html``;
        }
        return detailViewTemplate(this);
    }

    public changeProperties(): void {
        if (this.client) {
            const self: DetailView = this;
            this.client.fetchDetails(this)
                .then(function (): void {
                    self.requestUpdate().then(function () {
                        const reportLists: NodeListOf<ReportList> =
                            self.shadowRoot.querySelectorAll("report-list");
                        for (let i = 0; i < reportLists.length; i++) {
                            reportLists[i].refresh();
                        }
                    })
                })
                .catch(function (error: string | Error) {
                    console.log(error);
                });
        }
    }

    public async redraw(): Promise<void> {
        await this.requestUpdate();
        const lists: NodeListOf<ReportList> = this.shadowRoot.querySelectorAll("report-list");
        requestAnimationFrame(function () {
            for (let i = 0; i < lists.length; i++) {
                lists[i].requestUpdate();
            }
        });
    }
}
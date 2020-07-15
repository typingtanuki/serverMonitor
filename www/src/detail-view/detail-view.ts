import {
    CSSResult,
    customElement,
    html,
    LitElement,
    TemplateResult,
    unsafeCSS
} from 'lit-element';
import listStyle from "./detail-view.less";
import {RestClient} from "../rest/rest-client";
import {ReportList} from "../report-list/report-list";
import {
    detailViewTemplate,
    settingsViewTemplate,
    uploadViewTemplate
} from "./detail-view.template";
import {SettingsButton} from "../settings-button/settings-button";
import {buildForm} from "./form-manager";
import {Report, ServerInfo, Settings} from "../rest/types";

@customElement('detail-view')
export class DetailView extends LitElement {

    public static get dependencies(): any[] {
        return [ReportList, SettingsButton];
    }

    public static get styles(): CSSResult {
        return unsafeCSS(listStyle);
    }

    static get properties() {
        return {
            client: {type: Object},
            server: {type: Object},
            success: {type: Array},
            failure: {type: Array},
            settingsMode: {type: Boolean},
            uploadMode: {type: Boolean},
            settings: {type: Object}
        };
    }

    public client: RestClient = null;
    public server: ServerInfo = {name: "", monitors: []};
    public success: Report[] = [];
    public failure: Report[] = [];
    public settingsMode: boolean = false;
    public uploadMode: boolean = false;
    public settings: Settings = <any>{};

    constructor() {
        super();
    }

    public render(): TemplateResult {
        if (!this.server) {
            return html``;
        }
        if (this.settingsMode) {
            return settingsViewTemplate(this);
        }
        if (this.uploadMode) {
            return uploadViewTemplate(this);
        }
        return detailViewTemplate(this);
    }

    public changeProperties(): void {
        if (this.client && this.parentElement.classList.contains("half")) {
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
                    window.core.showError(error);
                    self.client = null;
                    window.core.closeDetails();
                });
        }
    }

    public async redraw(): Promise<void> {
        await this.requestUpdate();
        const lists: NodeListOf<ReportList> = this.shadowRoot.querySelectorAll(
            "report-list");
        requestAnimationFrame(function () {
            for (let i = 0; i < lists.length; i++) {
                lists[i].requestUpdate();
            }
        });
    }

    public showSettings(): void {
        this.settingsMode = true;
        this.client.fetchSettings(this)
            .catch(function (error: string | Error) {
                window.core.showError(error);
            });
    }

    public showUpdateMonitor(): void {
        this.uploadMode = true;
        this.requestUpdate();
    }

    public saveSettings(): void {
        buildForm(this.shadowRoot.querySelector(".settings"));
        this.client.saveSettings(this.settings)
            .catch(function (error: string | Error) {
                window.core.showError(error);
            });
    }

    public uploadBundle(): void {
        const formData: FormData = new FormData();
        this.addFiles("zip",
            <HTMLInputElement>this.shadowRoot.querySelector("#zip"),
            formData);
        this.addFiles("cert",
            <HTMLInputElement>this.shadowRoot.querySelector("#cert"),
            formData);

        this.client.uploadBundle(formData)
            .catch(function (error: string | Error) {
                window.core.showError(error);
            });
    }

    private addFiles(id: string, input: HTMLInputElement, form: FormData): void {
        for (const file of input.files) {
            form.append(id, file, file.name);
        }
        input.value = "";
    }
}
import {
    CSSResult,
    customElement,
    LitElement,
    property,
    TemplateResult,
    unsafeCSS
} from 'lit-element';
import reportStyle from "./report-list.less";
import {ReportEntry} from "../report-entry/report-entry";
import {MonitorType, Report} from "../rest/types";
import {reportListTemplate} from "./report-list-template";

@customElement('report-list')
export class ReportList extends LitElement {
    @property()
    public reports: Report[];
    public lastType: MonitorType | null = null;


    constructor() {
        super();
    }

    public static dependencies(): any[] {
        return [ReportEntry];
    }

    public static get styles(): CSSResult {
        return unsafeCSS(reportStyle);
    }

    public render(): TemplateResult {
        return reportListTemplate(this);
    }

    public refresh(): void {
        const entries: NodeListOf<ReportEntry> = this.shadowRoot.querySelectorAll(
            "report-entry");
        for (let i = 0; i < entries.length; i++) {
            entries[i].requestUpdate();
        }
    }
}
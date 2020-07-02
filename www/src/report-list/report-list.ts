import {CSSResult, customElement, html, LitElement, property, TemplateResult, unsafeCSS} from 'lit-element';
import reportStyle from "./report-list.less";
import {ReportEntry} from "../report-entry/report-entry";
import {Report} from "../rest/types";

@customElement('report-list')
export class ReportList extends LitElement {
    @property()
    public reports: Report[];


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
        return html`${this.reports.map(report => {
            return this.formatReport(report);
        })}`;
    }

    private formatReport(report: Report): TemplateResult {
        return html`<report-entry .report="${report}"></report-entry>`;
    }

    public refresh(): void {
        const entries: NodeListOf<ReportEntry> = this.shadowRoot.querySelectorAll("report-entry");
        for (let i = 0; i < entries.length; i++) {
            entries[i].requestUpdate();
        }
    }
}
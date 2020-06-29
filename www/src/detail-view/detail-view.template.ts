import {DetailView} from "./detail-view";
import {html, TemplateResult} from "lit-element";

export function detailViewTemplate(element: DetailView): TemplateResult {
    return html`
    <div id="server">${element.server.name}</div>
    <div id="response">
        <div id="settings">Settings</div>
        <report-list class="failure" .reports="${element.failure}"></report-list>
        <report-list class="success" .reports="${element.success}"></report-list>
    </div>`;
}
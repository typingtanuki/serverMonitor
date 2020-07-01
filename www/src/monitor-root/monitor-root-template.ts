import {MonitorRoot} from "./monitor-root";
import {html, TemplateResult} from "lit-element";
import {classMap} from "lit-html/directives/class-map";

export function monitorRootTemplate(monitorRoot: MonitorRoot): TemplateResult {
    return html`
<server-list .client="${monitorRoot.rootClient}" class="${classMap({half: monitorRoot.showDetails})}"></server-list>
<div class="${classMap({details: true, half: monitorRoot.showDetails})}">
    <detail-view .client="${monitorRoot.currentClient}" .server="${monitorRoot.currentServerInfo}"></detail-view>
</div>
<modal-root></modal-root>`;
}
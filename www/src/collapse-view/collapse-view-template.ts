import {DetailView} from "../detail-view/detail-view";
import {html, TemplateResult} from "lit-element";
import {CollapseView} from "./collapse-view";

export function collapseViewTemplate(collapse: CollapseView): TemplateResult {
    return html`
<div class="title" @click="${collapse.toggle}"><icon-svg .icon="${collapse.iconCollapse}"></icon-svg>${collapse.title}</div>
<div class="body"><slot></slot></div>`;
}
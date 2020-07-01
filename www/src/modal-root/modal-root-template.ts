import {ModalRoot} from "./modal-root";
import {html, TemplateResult} from "lit-element";

export function modalRootTemplate(modalRoot: ModalRoot): TemplateResult {
    return html`<div class="backdrop" @click="${modalRoot.clickBackdrop}"></div>
<div class="modal">
<div class="header" title="${modalRoot.header}">${modalRoot.header}</div>
<div class="content">${modalRoot.content}</div>
</div>`;
}
import {CSSResult, customElement, LitElement, TemplateResult, unsafeCSS} from 'lit-element';

import rootStyle from "./modal-root.less";
import {modalRootTemplate} from "./modal-root-template";

@customElement('modal-root')
export class ModalRoot extends LitElement {

    public static get styles(): CSSResult {
        return unsafeCSS(rootStyle);
    }

    static get properties() {
        return {
            header: {type: String},
            content: {type: Object}
        };
    }

    public header: string;
    public content: TemplateResult;

    constructor() {
        super();
    }

    public static get dependencies(): any[] {
        return [];
    }

    public render(): TemplateResult {
        return modalRootTemplate(this);
    }

    public clickBackdrop(): void {
        this.close();
    }

    public async open(): Promise<void> {
        await this.requestUpdate();
        this.classList.add("open");
    }

    public close(): void {
        this.classList.remove("open");
    }
}
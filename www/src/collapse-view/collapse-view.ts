import {CSSResult, customElement, html, LitElement, TemplateResult, unsafeCSS} from 'lit-element';
import progressStyle from "./collapse-view.less";

@customElement('collapse-view')
export class CollapseView extends LitElement {

    public static get styles(): CSSResult {
        return unsafeCSS(progressStyle);
    }

    static get properties() {
        return {
            title: {type: String}
        };
    }

    public title: string = "";
    public collapsed: boolean = true;

    constructor() {
        super();
    }

    public render(): TemplateResult {
        return html`
<div class="title" @click="${this.toggle}"><div class="icon gg-eye"></div>${this.title}</div>
<div class="body ${this.collapsed ? 'collapsed' : 'visible'}"><slot></slot></div>`;
    }

    public toggle(): void {
        this.collapsed = !this.collapsed;
        this.requestUpdate();
    }
}
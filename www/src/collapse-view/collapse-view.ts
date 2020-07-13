import {CSSResult, customElement, html, LitElement, property, TemplateResult, unsafeCSS} from 'lit-element';
import progressStyle from "./collapse-view.less";
import {icon, Icon, iconCollapse} from "../icon-svg/icons";

@customElement('collapse-view')
export class CollapseView extends LitElement {

    public static get styles(): CSSResult {
        return unsafeCSS(progressStyle);
    }

    @property({type: String})
    public title: string;
    @property({type: Boolean})
    public collapsed: boolean;
    @property({type: Object})
    private iconCollapse: Icon = icon(iconCollapse);

    constructor() {
        super();

        this.title = "";
        this.collapsed = true;
    }

    public render(): TemplateResult {
        return html`
<div class="title" @click="${this.toggle}"><icon-svg .icon="${this.iconCollapse}"></icon-svg>${this.title}</div>
<div class="body" ?collapsed=${this.collapsed}><slot></slot></div>`;
    }

    public attributeChangedCallback(name: string, oldVal: any, newVal: any): void {
        console.log('attribute change: ', name, newVal);
        super.attributeChangedCallback(name, oldVal, newVal);
    }

    public toggle(): void {
        this.collapsed = !this.collapsed;
        this.requestUpdate();
    }
}
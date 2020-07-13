import {CSSResult, customElement, html, LitElement, property, TemplateResult, unsafeCSS} from 'lit-element';
import iconStyle from "./icon-svg.less";
import {Icon} from "./icons";

@customElement('icon-svg')
export class IconSvg extends LitElement {
    @property() public icon: Icon | null = null;

    constructor(icon: Icon) {
        super();
        this.icon = icon;
    }

    public static get styles(): CSSResult {
        return unsafeCSS(iconStyle);
    }

    public render(): TemplateResult {
        return html`${this.iconSvg()}`;
    }

    private iconSvg(): string | HTMLElement {
        if (this.icon === null) {
            return "--";
        }
        return this.icon.build();
    }
}


import {CSSResult, customElement, html, LitElement, TemplateResult, unsafeCSS} from "lit-element";
import buttonStyle from "./settings-button.less";
import {ServerInfo} from "../rest/types";
import {Icon, icon, iconSettings} from "../icon-svg/icons";

@customElement('settings-button')
export class SettingsButton extends LitElement {

    public static get styles(): CSSResult {
        return unsafeCSS(buttonStyle);
    }

    static get properties() {
        return {
            server: {type: Object},
            label: {type: String},
            iconSettings: {type: Object}
        };
    }

    public server: ServerInfo;
    public label: string;
    private iconSettings: Icon = icon(iconSettings);

    constructor() {
        super();
    }

    public render(): TemplateResult {
        return html`<icon-svg .icon="${this.iconSettings}"></icon-svg>${this.label}`;
    }
}
import {CSSResult, customElement, html, LitElement, TemplateResult, unsafeCSS} from "lit-element";
import buttonStyle from "./settings-button.less";
import {ServerInfo} from "../rest/rest-client";

@customElement('settings-button')
export class SettingsButton extends LitElement {

    public static get styles(): CSSResult {
        return unsafeCSS(buttonStyle);
    }

    static get properties() {
        return {
            server: {type: Object}
        };
    }

    public server: ServerInfo;

    constructor() {
        super();
    }

    public render(): TemplateResult {
        return html`Settings`;
    }

    public firstUpdated(): void {
        const self: SettingsButton = this;
    }
}
import {colorNg, colorOk, colorTick} from "../constants/colors";

import {CSSResult, customElement, html, LitElement, TemplateResult, unsafeCSS} from 'lit-element';
import gaugeStyle from "./display-gauge.less";
import {isAttached} from "../layout";

@customElement('display-gauge')
export class DisplayGauge extends LitElement {
    public current: number;
    public max: number;

    constructor(current: number, max: number) {
        super();

        this.current = current;
        this.max = max;
    }

    static get properties() {
        return {
            current: {type: Number},
            max: {type: Number}
        };
    }

    public static get styles(): CSSResult {
        return unsafeCSS(gaugeStyle);
    }

    public render(): TemplateResult {
        return html`<div id="chart" class="gauge"></div>`;
    }

    public attributeChangedCallback(): void {
        this.rebuild();
    }

    public connectedCallback(): void {
        super.connectedCallback();
        this.rebuild();
    }

    private rebuild(): void {
        const options = this.makeOptions();
        const element: HTMLDivElement | null = this.shadowRoot.querySelector("#chart");
        const self: DisplayGauge = this;

        if (this.current !== undefined &&
            this.max !== undefined &&
            element !== null &&
            isAttached(this)) {
            requestAnimationFrame(function () {
                const gauge = echarts.init(element);
                gauge.setOption(options);
            });
        } else {
            requestAnimationFrame(function () {
                self.rebuild();
            })
        }
    }

    private makeOptions() {
        return {
            series: [
                {
                    type: 'gauge',
                    min: 0,
                    max: 100,
                    splitNumber: 10,
                    radius: '80%',
                    axisLine: {
                        lineStyle: {
                            color: [[0, colorOk], [this.max / 100, colorOk], [1, colorNg]],
                            width: 3,
                            shadowColor: colorTick,
                            shadowBlur: 10
                        }
                    },
                    axisLabel: {
                        fontWeight: 'bolder',
                        color: colorTick,
                        shadowColor: colorTick,
                        shadowBlur: 10
                    },
                    axisTick: {
                        length: 15,
                        lineStyle: {
                            color: 'auto',
                            shadowColor: colorTick,
                            shadowBlur: 10
                        }
                    },

                    splitLine: {
                        length: 10,
                        lineStyle: {
                            color: 'auto'
                        }
                    },
                    pointer: {
                        shadowColor: colorTick,
                        shadowBlur: 5
                    },
                    title: {
                        fontWeight: 'bolder',
                        fontSize: 10,
                        fontStyle: 'italic',
                        color: colorTick,
                        shadowColor: colorTick,
                        shadowBlur: 10
                    },
                    data: [{value: this.current}]
                }
            ]
        };
    }
}
import {colorNg, colorOk, colorTick} from "../constants/colors";

import {
    CSSResult,
    customElement,
    html,
    LitElement,
    property,
    TemplateResult,
    unsafeCSS
} from 'lit-element';
import lineStyle from "./display-line.less";
import {isAttached} from "../constants/mixins";

@customElement('display-line')
export class DisplayLine extends LitElement {
    @property() public values: string[];
    @property() public dates: string[];
    @property() public max: number;
    @property() public limit: number;

    constructor(values: string[], dates: string[], max: number, limit: number) {
        super();

        this.values = values;
        this.dates = dates;
        this.max = max;
        this.limit = limit;
    }

    public static get styles(): CSSResult {
        return unsafeCSS(lineStyle);
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
        const options: { [id: string]: any } = this.makeOptions();
        const element: HTMLDivElement | null = this.shadowRoot.querySelector("#chart");
        const self: DisplayLine = this;

        if (this.values !== undefined &&
            this.dates !== undefined &&
            this.max !== undefined &&
            this.limit !== undefined &&
            element !== null &&
            isAttached(this)) {
            requestAnimationFrame(function () {
                const gauge = echarts.init(element);
                gauge.setOption(<any>options);
            });
        } else {
            requestAnimationFrame(function () {
                self.rebuild();
            })
        }
    }

    private makeOptions(): { [id: string]: any } {
        const lineOption: { [id: string]: any } = {
            type: 'line',
            tooltip: {
                trigger: 'axis'
            },
            axisLabel: {
                fontWeight: 'bolder',
                color: colorTick,
                shadowColor: colorTick
            },
            axisTick: {
                lineStyle: {
                    color: 'auto',
                    shadowColor: colorTick
                }
            },
            xAxis: {
                type: 'category',
                data: this.dates,
                axisLabel: {
                    show: false
                }
            },
            yAxis: {
                type: 'value',
                min: 0,
                max: this.limit
            },
            series: {
                data: this.values,
                type: 'line',
                step: true,
                lineStyle: {
                    shadowColor: colorTick
                },
                color: {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [
                        {
                            offset: 0, color: colorNg
                        },
                        {
                            offset: 1 - (this.max / 100), color: colorOk
                        },
                        {
                            offset: 1, color: colorOk
                        }
                    ],
                    global: false // false by default
                },
                markLine: {
                    silent: true,
                    lineStyle: {
                        color: colorNg,
                        shadowColor: colorNg
                    },
                    data: [{
                        yAxis: this.max
                    }, {
                        yAxis: this.limit
                    }]
                }
            },
            pointer: {
                shadowColor: colorTick
            }
        };

        if (this.limit === -1) {
            lineOption.yAxis = {
                type: 'value'
            };
            lineOption.series.color = {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                    {
                        offset: 0, color: colorNg
                    },
                    {
                        offset: 0.2, color: colorOk
                    },
                    {
                        offset: 1, color: colorOk
                    }
                ],
                global: false // false by default
            };
            delete lineOption.series["markLine"];
        }
        return lineOption;
    }
}
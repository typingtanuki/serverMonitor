function makeLine(history, dates, max, limit) {
    const lineOption = {
        type: 'line',
        tooltip: {
            trigger: 'axis'
        },
        axisLabel: {
            fontWeight: 'bolder',
            color: colorTick,
            shadowColor: colorTick,
            shadowBlur: 10
        },
        axisTick: {
            lineStyle: {
                color: 'auto',
                shadowColor: colorTick,
                shadowBlur: 10
            }
        },
        xAxis: {
            type: 'category',
            data: dates,
            axisLabel: {
                show: false
            }
        },
        yAxis: {
            type: 'value',
            min: 0,
            max: limit
        },
        series: {
            data: history,
            type: 'line',
            step: true,
            lineStyle: {
                shadowColor: colorTick,
                shadowBlur: 10
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
                        offset: 1 - (max / 100), color: colorOk
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
                    shadowColor: colorNg,
                    shadowBlur: 10
                },
                data: [{
                    yAxis: max
                }, {
                    yAxis: limit
                }]
            }
        },
        pointer: {
            shadowColor: colorTick,
            shadowBlur: 5
        }
    };

    if (limit === -1) {
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

    const chart = document.createElement("div");
    chart.classList.add("line");

    requestAnimationFrame(function () {
        const line = echarts.init(chart);
        line.setOption(lineOption);
    });

    return chart;
}
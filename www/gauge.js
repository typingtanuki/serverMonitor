function makeGauge(current, max) {
    const gaugeOption = {
        series: [
            {
                name: '速度',
                type: 'gauge',
                min: 0,
                max: 100,
                splitNumber: 10,
                radius: '80%',
                axisLine: {
                    lineStyle: {
                        color: [[0, colorOk], [max / 100, colorOk], [1, colorNg]],
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
                data: [{value: current}]
            }
        ]
    };

    const chart = document.createElement("div");
    chart.classList.add("gauge");

    requestAnimationFrame(function () {
        const gauge = echarts.init(chart);
        gauge.setOption(gaugeOption);
    });

    return chart;
}
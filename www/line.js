function makeLine(history, dates, max) {
    const lineOption = {
        type: 'line',
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'category',
            data: dates
        },
        yAxis: {
            type: 'value',
            min: 0,
            max: 100
        },
        visualMap: {
            show: false,

            pieces: [{
                gt: 0,
                lte: max,
                color: colorOk,
            }, {
                gt: max,
                color: colorNg
            }]
        },
        series: {
            data: history,
            type: 'line',
            step: true,
            markLine: {
                silent: true,
                data: [{
                    yAxis: max
                }, {
                    yAxis: 100
                }]
            }
        },
        pointer: {
            shadowColor: colorTick,
            shadowBlur: 5
        }
    };

    const chart = document.createElement("div");
    chart.classList.add("line");

    requestAnimationFrame(function () {
        const line = echarts.init(chart);
        line.setOption(lineOption);
    });

    return chart;
}
function makeLine(history, dates, max, limit) {
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
            max: limit
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
        delete lineOption["visualMap"];
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
/**
 * Created by Tianshan on 18-1-2.
 */
// 基于准备好的dom，初始化echarts实例
//var AnnualChangeChart = echarts.init(document.getElementById('annual_change'), 'infographic');
var mediaChart = echarts.init(document.getElementById('media_chart'));
var identificationChart = echarts.init(document.getElementById('identification_chart'));
$(document).ready(function () {
    mediaChart.showLoading();
    identificationChart.showLoading();
    $.ajax({
        url: 'user/rest/count/group',
        type: 'GET',
        cache: false,
        processData: false,
        contentType: false,
        success: function (data) {
            var mediaChartOption = {
                tooltip: {},
                radar: {
                    // shape: 'circle',
                    name: {
                        textStyle: {
                            color: '#5a925a',
                            borderRadius: 3,
                            padding: [3, 5]
                        }
                    },
                    splitArea: {
                        areaStyle: {
                            color: ['rgba(90,146,90, 0.1)',
                                'rgba(90,146,90, 0.2)', 'rgba(90,146,90, 0.4)',
                                'rgba(90,146,90, 0.6)', 'rgba(90,146,90, 0.8)'],
                            shadowColor: 'rgba(0, 0, 0, 0.3)',
                            shadowBlur: 10
                        }
                    },
                    axisLine: {
                        lineStyle: {
                            color: 'rgba(255, 255, 255, 0.5)'
                        }
                    },
                    splitLine: {
                        lineStyle: {
                            color: 'rgba(255, 255, 255, 0.5)'
                        }
                    },
                    indicator: data.indicator,
                    center: ['50%', '50%'],
                    shape: 'circle',
                },
                series: [{
                    type: 'radar',
                    // areaStyle: {normal: {}},
                    itemStyle: {
                        normal: {
                            color: "rgba(0,0,0,0)", // 图表中各个图区域的边框线拐点颜色
                        }
                    },
                    data: [
                        {
                            value: data.value,
                            name: chart1_name,
                            lineStyle: {
                                normal: {
                                    color: 'rgba(255, 255, 255, 0.5)'
                                }
                            },
                            areaStyle: {
                                normal: {
                                    color: 'rgba(47, 79, 79, 0.5)'
                                }
                            }
                        }
                    ]
                }]
            };
            // 使用刚指定的配置项和数据显示图表。
            mediaChart.hideLoading();
            mediaChart.clear();
            mediaChart.setOption(mediaChartOption);
        },
        error: function (data) {

        }
    });
    $.ajax({
        url: 'user/rest/count/idrecord',
        type: 'GET',
        cache: false,
        processData: false,
        contentType: false,
        success: function (data) {
            var identificationChartOption = {
                tooltip: {},
                calculable: true,
                color: ['#439357', '#749317', '#788471', '#4ea397', '#BDB552', '#647692', '#c4cf5d', '#379876'],//自己设置扇形图颜色
                series: [
                    {
                        name: '',
                        type: 'pie',
                        radius: [30, 110],
                        roseType: 'area',
                        data: data
                    }
                ]
            };
            // 使用刚指定的配置项和数据显示图表。
            identificationChart.hideLoading();
            identificationChart.clear();
            identificationChart.setOption(identificationChartOption);
        },
        error: function (data) {

        }
    });
});
//
// $('#statistics_select').change(function(){
//     var select = $("#statistics_select").find("option:selected").val();
//     var url = "super/data/viewRecord";
//     if (select=="species")
//     {
//         url="super/data/viewRecordSpecies";
//     }
//     else if (select=="all")
//     {
//         url="super/data/viewRecord";
//     }
//     clickChart.showLoading();
//     $.get(url).done(function (data) {
//         clickChart.hideLoading();
//         // 填入数据
//         // 指定图表的配置项和数据
//
//         var date = data.recordDate;
//         var data = data.recordData;
//
//         var clickOption = {
//             tooltip: {
//                 trigger: 'axis',
//                 position: function (pt) {
//                     return [pt[0], '10%'];
//                 }
//             },
//             title: {
//                 left: 'center',
//                 text: '点击数据量面积图',
//             },
//             toolbox: {
//                 feature: {
//                     dataZoom: {
//                         yAxisIndex: 'none'
//                     },
//                     restore: {},
//                     saveAsImage: {}
//                 }
//             },
//             xAxis: {
//                 type: 'category',
//                 boundaryGap: false,
//                 data: date
//             },
//             yAxis: {
//                 type: 'value',
//                 boundaryGap: [0, '100%']
//             },
//             dataZoom: [{
//                 type: 'inside',
//                 start: 0,
//                 end: 10
//             }, {
//                 start: 0,
//                 end: 10,
//                 handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
//                 handleSize: '80%',
//                 handleStyle: {
//                     color: '#fff',
//                     shadowBlur: 3,
//                     shadowColor: 'rgba(0, 0, 0, 0.6)',
//                     shadowOffsetX: 2,
//                     shadowOffsetY: 2
//                 }
//             }],
//             series: [
//                 {
//                     name:'点击量',
//                     type:'line',
//                     smooth:true,
//                     symbol: 'none',
//                     sampling: 'average',
//                     itemStyle: {
//                         normal: {
//                             color: 'rgb(55, 55, 155)'
//                         }
//                     },
//                     areaStyle: {
//                         normal: {
//                             color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
//                                 offset: 0,
//                                 color: 'rgb(0, 158, 220)'
//                             }, {
//                                 offset: 1,
//                                 color: 'rgb(0, 70, 131)'
//                             }])
//                         }
//                     },
//                     data: data
//                 }
//             ]
//         };
//         // 使用刚指定的配置项和数据显示图表。
//         clickChart.setOption(clickOption);
//     });
//     clickChart.clear();
//     clickChart.setOption(clickOption);
// });clickOption



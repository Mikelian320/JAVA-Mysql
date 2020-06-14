const colorList = ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3'];

const colorFormat = params => colorList[params.dataIndex % colorList.length];

export default (statistics) => {
  let sum = 0;
  const data = Object.entries(statistics).map(([key, value]) => {
    sum += +value;
    return {
      name: key,
      value,
    };
  }).sort((a, b) => +b.value - +a.value);
  return {
    title: {
      text: '数据库记录统计',
      subtext: `总计 ${sum}`,
      left: '50%',
      textAlign: 'center',
      textStyle: {
        fontSize: '24',
      },
      subtextStyle: {
        color: '#c23531',
        fontWeight: 600,
        fontSize: '18',
      },
    },
    tooltip: {},
    xAxis: {
      type: 'category',
      data: data.map(item => item.name),
      axisLabel: {
        interval: 0,
      },
    },
    yAxis: {
      type: 'value',
    },
    grid: [{
      top: '20%',
      bottom: '5%',
      width: '40%',
      containLabel: true,
    }],
    series: [{
      type: 'bar',
      tooltip: {
        show: false,
      },
      label: {
        normal: {
          position: 'top',
          show: true,
        },
      },
      itemStyle: {
        normal: {
          color: colorFormat,
        },
      },
      data: data.map(item => +item.value),
    }, {
      type: 'pie',
      radius: [0, '60%'],
      center: ['70%', '55%'],
      data,
      clockwise: false,
      tooltip: {
        formatter: '{b}: {d}%',
      },
      label: {
        lineHeight: 18,
        position: 'outside',
        formatter: '{b}\n{d}%',
      },
      itemStyle: {
        normal: {
          color: colorFormat,
        },
      },
    }],
  };
};

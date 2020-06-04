<template>
  <v-chart :options="options"/>
</template>

<style>
.echarts {
  width: 100%;
  min-height: 400px;
}
</style>

<script>
import ECharts from 'vue-echarts';
import 'echarts/lib/chart/pie';
import 'echarts/lib/chart/bar';
import 'echarts/lib/component/title';
import 'echarts/lib/component/tooltip';

import axios from 'axios';
import { SEARCH_ORIGIN, SEARCH_MODE } from '@/constants/config';

import getOptions from './getOptions';

export default {
  components: {
    'v-chart': ECharts,
  },
  data() {
    return {
      options: {},
      statistics: {},
    };
  },
  async created() {
    try {
      const res = await axios.get(`${SEARCH_ORIGIN}searchdata`, {
        params: {
          searchMode: SEARCH_MODE.SUM_TEST_RECORD,
        },
      });
      this.statistics = res.data;
      this.options = getOptions(this.statistics);
    } catch (error) {
      this.$message.error('数据出错了~');
    }
  },
};
</script>

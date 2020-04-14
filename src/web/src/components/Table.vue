<template>
  <div class="container">
    <div class="left-box">
      <searchForm />
      <el-table
        border
        :data="tableData"
        :key="tableKey"
        height="500"
        width="100%"
        v-loading="loading"
        element-loading-text="拼命加载中"
        @row-dblclick="handleClick"
        v-el-table-infinite-scroll="load"
      >
        <el-table-column prop="slot" label="槽位"></el-table-column>
        <el-table-column prop="test_site" label="测试站点" width="120"></el-table-column>
        <el-table-column prop="test_requirement" label="测试需求" width="120"></el-table-column>
        <el-table-column prop="product_model" label="产品型号" width="160"></el-table-column>
        <el-table-column prop="serial_number" label="序列号" width="160"></el-table-column>
        <el-table-column prop="mac" label="MAC地址" width="160"></el-table-column>
        <el-table-column prop="test_time" label="测试时间" width="180"></el-table-column>
        <el-table-column prop="result" label="测试结果">
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.result === 'PASS' ? 'success' : 'danger'"
              disable-transitions>{{scope.row.result === 'PASS' ? '成功' :'失败'}}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-card class="box-card" v-loading="log.loading">
      <div slot="header">
        <span>{{ log.title }}</span>
        <el-dropdown
          style="float: right; padding: 3px 0; cursor: pointer;"
          placement="bottom"
          trigger="click"
          @command="handleCommand"
        >
          <span class="el-dropdown-link">
            <i class="el-icon-more"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item
              command="download"
              icon="el-icon-download"
              :disabled="log.content === ''"
            >下载</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <el-input
        readonly
        type="textarea"
        :rows="27"
        placeholder="双击某一行查看日志"
        v-model="log.content">
      </el-input>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios';
import { SEARCH_ORIGIN, LOAD_DATA_ENTER_PAGE } from '@/constants/config';
import downloadData from '@/utils/downloadData';
import elTableInfiniteScroll from 'el-table-infinite-scroll';
import SearchForm from './SearchForm.vue';
import checkLogin from '@/utils/checkLogin';

const initPaginationInfo = {
  offset: 0,
  limit: 50,
  noMore: false,
};

export default {
  created() {
    const username = localStorage.getItem('username');
    const password = localStorage.getItem('password');
    if (!checkLogin(username, password)) {
      this.$message.error('You need login first!');
      setTimeout(() => {
        this.$router.push('/login');
      }, 500);
      return;
    }
    if (LOAD_DATA_ENTER_PAGE) {
      this.searchData();
    }
  },
  directives: {
    'el-table-infinite-scroll': elTableInfiniteScroll,
  },
  data() {
    const data = [];
    return {
      tableData: data.map(item => ({
        slot: item[0],
        test_site: item[1],
        test_requirement: item[2],
        product_model: item[3],
        serial_number: item[4],
        mac: item[5],
        test_host: item[6],
        ate: item[7],
        hardware_version: item[8],
        software_version: item[9],
        software_serial: item[10],
        boot_version: item[12],
        result: item[13],
      })),
      loading: false,
      log: {
        loading: false,
        title: '日志',
        content: '',
      },
      selectedRow: undefined,
      pagination: initPaginationInfo,
      searchParams: {},
      tableKey: new Date().valueOf(),
    };
  },
  methods: {
    load() {
      if (this.tableData.length === 0) {
        // 无数据时不加载数据，必须通过 search 触发一次后才能调用 load
        return;
      }
      if (this.pagination.noMore) {
        this.$message('无更多数据');
        return;
      }
      if (!this.loading) {
        this.pagination = {
          offset: this.pagination.offset + this.pagination.limit,
          limit: 10,
        };
        this.pagination.limit = 10;
        this.searchData();
        console.log('TODO: load more');
      }
    },
    handleCommand(command) {
      switch (command) {
        case 'download':
          if (this.selectedRow) {
            const log = this.log.content;
            const {
              test_time,
              serial_number,
              product_model,
              test_site,
              mac,
              result,
              test_requirement,
            } = this.selectedRow;
            const isWin = navigator.platform === 'Win32'
              || navigator.platform === 'Windows';
            const blobLog = isWin ? log.replace(/\n/g, '\r\n') : log;
            const name = `${product_model}_${test_site}_${test_requirement}_${serial_number}_${mac}_${result}_${test_time}`;
            const suffix = 'txt';
            downloadData(blobLog, `${name}.${suffix}`);
          }
          break;

        default:
          break;
      }
    },
    setSearchParams(params) {
      let newParams;
      if (params) {
        newParams = { ...params } || {};
        Object.entries(newParams).forEach(([key, value]) => {
          if (!value) {
            delete newParams[key];
          }
        });
      }
      this.pagination = initPaginationInfo;
      this.searchParams = newParams;
      this.tableKey = new Date().valueOf();
      this.searchData();
    },
    async searchData() {
      if (this.loading) {
        this.$message('正在查询中');
        return;
      }
      this.loading = true;
      try {
        const res = await axios.get(`${SEARCH_ORIGIN}searchdata`, {
          params: {
            searchMode: 'ProductInfo',
            Offset: this.pagination.offset,
            Limit: this.pagination.limit,
            ...this.searchParams,
          },
        });
        const data = res.data.map(item => ({
          slot: item[0],
          test_site: item[1],
          test_requirement: item[2],
          product_model: item[3],
          serial_number: item[4],
          mac: item[5],
          test_time: item[6],
          test_host: item[7],
          ate: item[8],
          hardware_version: item[9],
          software_version: item[10],
          software_serial: item[11],
          boot_version: item[12],
          result: item[13],
        }));
        if (this.pagination.offset === 0) {
          this.tableData = data;
        } else {
          this.tableData = this.tableData.concat(data);
        }

        if (data.length === 0) {
          this.$message('无更多数据');
          this.pagination.noMore = true;
        }
      } catch (error) {
        this.$message.error('数据出错了~');
      }
      this.loading = false;
    },
    async handleClick(row) {
      const {
        slot, test_host, test_time, serial_number,
      } = row;

      if (this.log.loading) {
        return;
      }

      this.log.loading = true;

      try {
        const res = await axios.get(`${SEARCH_ORIGIN}searchdata`, {
          params: {
            searchMode: 'Log',
            SN: serial_number,
            PC_Name: test_host,
            Record_Time: new Date(test_time).valueOf(),
            Slot: slot,
          },
        });
        const [[log]] = res.data;
        this.log.loading = false;

        this.log.content = log.replace(/\r/g, '\n');
        this.selectedRow = row;
      } catch (error) {
        this.log.loading = false;
        this.$message.error('数据出错了~');
      }
    },
  },
  components: {
    SearchForm,
  },
};
</script>
<style scoped>
.container {
  display: flex;
}
.container .el-textarea {
  margin-top: 10px;
}
.left-box {
  margin-left: 1%;
  width: 66%;
}
.box-card {
  width: 30%;
  margin-left: 2%;
  overflow-y: auto;
}
</style>

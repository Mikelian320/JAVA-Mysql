<template>
  <div class="container">
    <div class="left-box">
      <searchForm />
      <p class="find-result">
        查询到 <span style="color: #409EFF">{{total}}</span> 条数据
        <el-button @click="handleDownloadPage">下载本页日志</el-button>
      </p>
      <el-table
        ref="table"
        border
        :data="tableData"
        :key="tableKey"
        height="500"
        width="100%"
        v-loading="loading"
        element-loading-text="拼命加载中"
        @row-dblclick="handleClick"
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
        <el-pagination
          :total="total"
          :page-size="pagination.limit"
          :page-sizes="[50, 100, 200]"
          :current-page="pagination.page"
          @current-change="pageChange"
          @size-change="pageSizeChange"
          background
          layout="sizes, prev, pager, next"
          >
        </el-pagination>
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
  page: 1,
  limit: 50,
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
      this.searchData(true);
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
      total: 0,
      searchParams: {},
      tableKey: new Date().valueOf(),
    };
  },
  methods: {
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
      this.setPagination({
        page: 1,
      });
      this.searchParams = newParams;
      this.tableKey = new Date().valueOf();
      this.searchData(true);
    },
    async getTotal(searchWithTotal) {
      if (!searchWithTotal) {
        return this.total;
      }
      const res = await axios.get(`${SEARCH_ORIGIN}searchdata`, {
        params: {
          searchMode: 'TestCount',
          ...this.searchParams,
        },
      });
      const [[total]] = res.data;
      return +total;
    },
    async searchData(searchWithTotal) {
      if (this.loading) {
        this.$message('正在查询中');
        return;
      }
      this.loading = true;
      try {
        const { limit, page } = this.pagination;
        const allRequest = [
          axios.get(`${SEARCH_ORIGIN}searchdata`, {
            params: {
              searchMode: 'ProductInfo',
              Offset: (page - 1) * limit,
              Limit: limit,
              ...this.searchParams,
            },
          }),
          this.getTotal(searchWithTotal),
        ];
        const [res, total] = await Promise.all(allRequest);
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
        this.tableData = data;
        this.total = total;
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
    setPagination(params) {
      this.pagination = {
        ...this.pagination,
        ...params,
      };
    },
    pageChange(page) {
      this.setPagination({
        page,
      });
      this.searchData();
      this.$refs.table.bodyWrapper.scrollTop = 0;
    },
    pageSizeChange(size) {
      this.setPagination({
        page: 1,
        limit: size,
      });
      this.searchData();
      this.$refs.table.bodyWrapper.scrollTop = 0;
    },
    async handleDownloadPage() {
      try {
        const { limit, page } = this.pagination;
        await axios.get(`${SEARCH_ORIGIN}searchdata`, {
          params: {
            searchMode: 'PackageTestLogs',
            Offset: (page - 1) * limit,
            Limit: limit,
            ...this.searchParams,
          },
        });
      } catch (error) {
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

.find-result {
  text-align: left;
}
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

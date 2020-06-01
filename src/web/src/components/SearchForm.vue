<template>
  <el-form
    :model="formInline"
    :rules="rules"
    label-position="right"
    label-width="80px"
    ref="ruleForm"
    class="demo-form-inline"
    @submit.native.prevent
    @keyup.enter.native="submitForm('ruleForm')"
  >
    <div class="simple-search">
      <el-form-item label="序列号" prop="SN" :rules="rules.SN">
        <el-input v-model="formInline.SN" placeholder="请输入13位序列号" clearable />
        <transition name="button">
          <el-button v-if="!state.showAdvance" type="primary" @click="submitForm('ruleForm')">
            查询
            </el-button>
        </transition>
      </el-form-item>
      <el-form-item class="search-more">
        <el-button type="text" @click="toggleSearchForm">
          高级搜素
          <i class="el-icon-arrow-right" :class="{ 'is-active': state.showAdvance }" />
        </el-button>
      </el-form-item>
    </div>
    <transition name="advance-box">
      <el-row v-if="state.showAdvance">
        <el-col :span="8" :xs="24" :sm="12" :lg="8" :xl="6">
          <el-form-item label="MAC" prop="mac" :rules="rules.MAC">
            <el-input v-model="formInline.mac" placeholder="请输入12位MAC" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8" :xs="24" :sm="12" :lg="8" :xl="6">
          <el-form-item label="产品型号">
            <el-input v-model="formInline.serial"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8" :xs="24" :sm="12" :lg="8" :xl="6">
          <el-form-item label="测试需求">
            <el-input v-model="formInline.testRequire"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8" :xs="24" :sm="12" :lg="8" :xl="6">
          <el-form-item label="产品类型">
            <el-select v-model="formInline.type" placeholder="全部">
              <el-option label="全部" value></el-option>
              <el-option label="无线控制器" value="ac"></el-option>
              <el-option label="中低端交换机" value="ml_switch"></el-option>
              <el-option label="中低端路由器" value="ml_router"></el-option>
              <el-option label="高端交换机" value="h_switch"></el-option>
              <el-option label="高端路由器" value="h_router"></el-option>
              <el-option label="安全产品" value="securitypro"></el-option>
              <el-option label="无线产品" value="ap"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8" :xs="24" :sm="12" :lg="8" :xl="6">
          <el-form-item label="测试站点">
            <el-autocomplete
              clearable
              class="inline-input"
              v-model="formInline.site"
              :fetch-suggestions="siteSuggestions"
              placeholder="请输入测试站点"
              @select="handleSelectSite"
            ></el-autocomplete>
          </el-form-item>
        </el-col>
        <el-col :span="8" :xs="24" :sm="12" :lg="8" :xl="6">
          <el-form-item label="测试结果">
            <el-select v-model="formInline.result" placeholder="全部">
              <el-option label="全部" value></el-option>
              <el-option label="通过" value="PASS"></el-option>
              <el-option label="失败" value="FAIL"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" :xs="24" :sm="18" :lg="12">
          <el-form-item label="测试时间">
            <el-date-picker
              v-model="formInline.dateRange"
              type="datetimerange"
              align="right"
              unlink-panels
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="pickerOptions"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">查询</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </transition>
  </el-form>
</template>
<script>

const siteOptions = [
  { value: 'SETMAC' },
  { value: '基本测试' },
  { value: '基本测试1' },
  { value: '查看烤机记录' },
  { value: '查看烤机记录1' },
  { value: '下装主程序' },
  { value: '总检' },
  { value: '连通' },
  { value: 'OQC按单重检' },
  { value: '下装生测程序' },
  { value: '吞吐测试' },
];

export default {
  data() {
    return {
      state: {
        showAdvance: false,
      },
      formInline: {
        SN: '',
        mac: '',
        serial: '',
        type: '',
        site: '',
        result: '',
        dateRange: [],
        testRequire: '',
      },
      rules: {
        SN: [
          {
            validator(rule, value, callback) {
              if (!value || value.length === 13) {
                callback();
              } else {
                callback('请输入13位序列号');
              }
            },
            trigger: 'blur',
          },
        ],
        MAC: [
          {
            validator(rule, value, callback) {
              if (!value || value.length === 12) {
                callback();
              } else {
                callback('请输入12位MAC');
              }
            },
            trigger: 'blur',
          },
        ],
      },
      pickerOptions: {
        shortcuts: [
          {
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            },
          },
          {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            },
          },
          {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            },
          },
        ],
      },
    };
  },
  methods: {
    siteSuggestions(querystring, cb) {
      const result = siteOptions.filter(option => option.value.includes(querystring));
      cb(result);
    },
    handleSelectSite(option) {
      this.formInline.site = option.value;
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const {
            SN, mac, type, site, serial, result, testRequire,
          } = this.$data.formInline;
          let { dateRange } = this.$data.formInline;
          // fix dateRange is null
          dateRange = dateRange || [];
          const [start, end] = dateRange;
          const parent = this.$parent;

          parent.setSearchParams({
            SN: SN && SN.trim(),
            MAC: mac && mac.trim(),
            Product_Model: serial && serial.trim(),
            Product_Type: type,
            Test_Station: site,
            Test_Require: testRequire,
            StartTime: start && start.valueOf(),
            EndTime: end && end.valueOf(),
            TestResult: result,
          });
          return true;
        }
        return false;
      });
    },
    toggleSearchForm() {
      this.state.showAdvance = !this.state.showAdvance;
    },
  },
};
</script>
<style>
.inline-input {
  width: 100%
}
.demo-form-inline {
  text-align: left;
  border-top: 1px solid #ebeef5;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 15px;
}
.demo-form-inline .simple-search {
  margin-top: 22px;
  display: flex;
}
.demo-form-inline .simple-search .el-form-item {
  white-space: nowrap;
}
.demo-form-inline .simple-search .el-button {
  margin-left: 15px;
}
.demo-form-inline .simple-search .search-more {
  margin-left: auto;
  margin-right: 5px;
}
.demo-form-inline .simple-search .el-icon-arrow-right {
  transition: transform 0.5s;
}
.demo-form-inline .simple-search .el-icon-arrow-right.is-active {
  transform: rotate(90deg);
}
.demo-form-inline .el-select,
.demo-form-inline .el-range-editor{
  width: 100%;
}
.demo-form-inline .button-enter-active,
.demo-form-inline .button-leave-active {
  transition: all 1s;
}

.demo-form-inline .button-enter,
.demo-form-inline .button-leave-to {
  opacity: 0;
}

.demo-form-inline .advance-box-enter-active,
.demo-form-inline .advance-box-leave-active {
  transition: all 0.5s;
  max-height: 300px;
  overflow: hidden;
}
.demo-form-inline .advance-box-enter,
.demo-form-inline .advance-box-leave-to {
  max-height: 0;
}
</style>

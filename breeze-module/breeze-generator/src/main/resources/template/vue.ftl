<template>
    <basic-container>
        <el-row>
            <el-col style='margin-top:15px;'>
                <avue-crud :option="option"
                           :data="list"
                           :page="page"
                           :permission="permissionList"
                           ref="crud"
                           v-model="form"
                           :table-loading="listLoading"
                           @on-load="getList"
                           @refresh-change="handleRefreshChange"
                           @row-update="update"
                           @row-save="create"
                           @row-del="rowDel"
                           :before-open="handleOpenBefore">
                </avue-crud>
            </el-col>
        </el-row>
    </basic-container>
</template>

<script>
  import {addObj, delObj, fetchList, putObj} from '@/api/admin/${code_function_low}'
  import {tableOption} from '@/const/crud/admin/${code_function_low}'
  import {mapGetters} from 'vuex'

  export default {
    name: 'breeze${code_function}',
    data() {
      return {
        // table配置
        option: tableOption,
        // table数据
        list: [],
        // form表单数据
        form: {},
        // 分页参数
        page: {
          currentPage: 1, // 当前页数
          pageSize: 20, // 每页显示多少条
          total: 0 // 总页数
        },
        // 加载遮罩开关
        listLoading: true
      }
    },
    created() {
    },
    computed: {
      ...mapGetters(['permissions']),
      // 根据权限设置开关是否展示
      permissionList() {
        return {
          addBtn: this.vaildData(this.permissions.${code_function_low}_add, false),
          editBtn: this.vaildData(this.permissions.${code_function_low}_edit, false),
          delBtn: this.vaildData(this.permissions.${code_function_low}_del, false)
        };
      }
    },
    methods: {
      /**
       * 获取数据分页列表
       */
      getList(page, params) {
        fetchList(Object.assign({
          page: page.currentPage,
          pageSize: page.pageSize
        }, params)).then(response => {
          this.list = response.dataList;
          this.page.total = response.dataTotal;
          this.listLoading = false;
        });
      },
      /**
       * 刷新列表
       */
      handleRefreshChange() {
        this.page = {
          currentPage: 1,
          pageSize: 20,
          total: 0
        };
        this.getList(this.page);
      },
      /**
       * 打开单条数据模态窗之前的操作
       * @param done
       */
      handleOpenBefore(done) {
        done();
      },
      /**
       * 新增数据
       * @param row
       * @param done
       * @param loading
       */
      create(row, done, loading) {
        addObj(row).then(() => {
          this.handleRefreshChange();
          done();
          this.$notify.success('创建成功')
        }).catch(() => {
          loading();
        });
      },
      /**
       * 修改数据
       * @param row
       * @param done
       * @param loading
       */
      update(row, index, done, loading) {
        putObj(row).then(() => {
          this.handleRefreshChange();
          done();
          this.$notify.success('修改成功')
        }).catch(() => {
          loading();
        });
      },
      /**
       * 删除数据
       * @param row
       */
      rowDel(row) {
        this.$confirm("确定将选择数据删除?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          return delObj(row.${primary_key_upper});
        }).then(() => {
          this.handleRefreshChange();
          this.$message.success("删除成功");
        });
      }
    }
  }
</script>
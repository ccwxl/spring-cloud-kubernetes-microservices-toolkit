<template>
  <div class="common-layout">
    <el-container style="height: 100%;">
      <el-aside width="200px">配置文件列表页面</el-aside>
      <el-container>
        <el-header>页面详情页</el-header>
        <el-main>编译配置页面</el-main>
      </el-container>
      <el-aside width="200px">文件变更历史页面</el-aside>
    </el-container>
  </div>
</template>

<style lang="css">
.common-layout {
  height: 100%;
}

.common-layout .el-aside {
  background-color: var(--el-color-primary-light-8);
  color: var(--el-text-color-primary);
  text-align: center;
}

.common-layout .el-header, .common-layout .el-footer {
  background-color: var(--el-color-primary-light-7);
  color: var(--el-text-color-primary);
  text-align: center;
}

.common-layout .el-main {
  background-color: var(--el-color-primary-light-9);
  color: var(--el-text-color-primary);
  text-align: center;
  height: 150px;
}
</style>
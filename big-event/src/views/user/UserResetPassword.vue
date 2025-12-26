<script setup>
import { ref } from 'vue'
import { userUpdatePasswordService } from '@/api/user.js'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()

// 表单数据
const form = ref({
  old_pwd: '',
  new_pwd: '',
  re_pwd: ''
})

// 表单验证规则
const rules = {
  old_pwd: [
    { required: true, message: '请输入原密码', trigger: 'blur' },
    { min: 6, max: 16, message: '密码长度应在6-16位之间', trigger: 'blur' }
  ],
  new_pwd: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 16, message: '密码长度应在6-16位之间', trigger: 'blur' }
  ],
  re_pwd: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: (rule, value, callback) => {
      if (value !== form.value.new_pwd) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }, trigger: 'blur' }
  ]
}

const updatePassword = async (formRef) => {
  // 验证表单
  try {
    // 检查两次输入的新密码是否一致
    if (form.value.new_pwd !== form.value.re_pwd) {
      ElMessage.error('两次输入的新密码不一致')
      return
    }

    // 调用API更新密码
    const result = await userUpdatePasswordService(form.value.old_pwd, form.value.new_pwd, form.value.re_pwd)
    
    if (result.code === 0) {
      ElMessage.success(result.msg || '密码修改成功')
      // 跳转到登录页
      router.push('/login')
    } else {
      ElMessage.error(result.msg || '密码修改失败')
    }
  } catch (error) {
    console.error('密码修改失败', error)
    ElMessage.error('密码修改失败，请稍后重试')
  }
}
</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>重置密码</span>
      </div>
    </template>
    <el-row>
      <el-col :span="12" :offset="6">
        <el-form 
          :model="form" 
          :rules="rules" 
          ref="formRef" 
          label-width="100px" 
          style="max-width: 400px"
        >
          <el-form-item label="原密码" prop="old_pwd">
            <el-input v-model="form.old_pwd" type="password" placeholder="请输入原密码" />
          </el-form-item>
          <el-form-item label="新密码" prop="new_pwd">
            <el-input v-model="form.new_pwd" type="password" placeholder="请输入新密码" />
          </el-form-item>
          <el-form-item label="确认新密码" prop="re_pwd">
            <el-input v-model="form.re_pwd" type="password" placeholder="请再次输入新密码" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="updatePassword(formRef)">提交</el-button>
            <el-button @click="form = { old_pwd: '', new_pwd: '', re_pwd: '' }">重置</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </el-card>
</template>

<style lang="scss" scoped>
.page-container {
  min-height: 100%;
  box-sizing: border-box;

  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}
</style>
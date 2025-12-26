<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'

import { ref } from 'vue'

//文章分类数据模型
const categorys = ref([
    {
        "id": 3,
        "categoryName": "美食",
        "categoryAlias": "my",
        "createTime": "2023-09-02 12:06:59",
        "updateTime": "2023-09-02 12:06:59"
    },
    {
        "id": 4,
        "categoryName": "娱乐",
        "categoryAlias": "yl",
        "createTime": "2023-09-02 12:08:16",
        "updateTime": "2023-09-02 12:08:16"
    },
    {
        "id": 5,
        "categoryName": "军事",
        "categoryAlias": "js",
        "createTime": "2023-09-02 12:08:33",
        "updateTime": "2023-09-02 12:08:33"
    }
])

//用户搜索时选中的分类id
const categoryId = ref('')

//用户搜索时选中的发布状态
const state = ref('')

//文章列表数据模型
const articles = ref([])

//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(0)//总条数
const pageSize = ref(3)//每页条数

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    articleList()
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    articleList()
}

//回显文章分类
import { articleCategoryListService, articleListService, articleAddService, articleDeleteService, articleDeleteBatchService } from '@/api/article.js'
const articleCategoryList = async () => {
    let result = await articleCategoryListService();

    categorys.value = result.data;
}

//获取文章列表数据
const articleList = async () => {
    // 显示加载状态
    loading.value = true;
    
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        categoryId: categoryId.value ? categoryId.value : null,
        state: state.value ? state.value : null
    }
    
    try {
        let result = await articleListService(params);

        //渲染视图
        total.value = result.data.total;
        articles.value = result.data.items;

        //处理数据,给数据模型扩展一个属性categoryName,分类名称
        articles.value.forEach(article => {
            const category = categorys.value.find(cat => article.categoryId === cat.id);
            if (category) {
                article.categoryName = category.categoryName;
            }
        });
    } catch (error) {
        console.error('获取文章列表失败:', error);
    } finally {
        // 隐藏加载状态
        loading.value = false;
    }
}

// 添加loading状态
const loading = ref(false);


articleCategoryList()
articleList();

import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { Plus } from '@element-plus/icons-vue'
//控制抽屉是否显示
const visibleDrawer = ref(false)
//添加表单数据模型
const articleModel = ref({
    title: '',
    categoryId: '',
    coverImg: '',
    content: '',
    state: ''
})


//导入token
import { useTokenStore } from '@/stores/token.js';
const tokenStore = useTokenStore();

//上传成功的回调函数
const uploadSuccess = (result)=>{
    articleModel.value.coverImg = result.data;
    console.log(result.data);
}

//添加文章
import {ElMessage, ElMessageBox} from 'element-plus'
import { articleUpdateService, articleDetailService } from '@/api/article.js'

// 添加或更新文章
const submitArticle = async (clickState) => {
    //把发布状态赋值给数据模型
    articleModel.value.state = clickState;

    // 验证必填字段
    if (!articleModel.value.title?.trim()) {
        ElMessage.error('请输入文章标题');
        return;
    }
    if (!articleModel.value.categoryId) {
        ElMessage.error('请选择文章分类');
        return;
    }
    if (!articleModel.value.coverImg?.trim()) {
        ElMessage.error('请上传文章封面');
        return;
    }
    if (!articleModel.value.content?.trim()) {
        ElMessage.error('请输入文章内容');
        return;
    }

    let result;
    if (articleModel.value.id) {
        // 更新文章
        result = await articleUpdateService(articleModel.value);
        ElMessage.success('更新成功');
    } else {
        // 添加文章
        result = await articleAddService(articleModel.value);
        ElMessage.success('添加成功');
    }

    //让抽屉消失
    visibleDrawer.value = false;

    //刷新当前列表
    articleList()
}

//批量删除相关
const selectedIds = ref([])

// 选择行时触发
const handleSelectionChange = (selection) => {
    selectedIds.value = selection.map(item => item.id)
}

// 单个删除文章
const deleteArticle = async (id) => {
    try {
        await ElMessageBox.confirm('确定要删除这篇文章吗？', '确认删除', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
        
        // 调用删除接口
        const result = await articleDeleteService(id)
        ElMessage.success('删除成功')
        
        // 刷新列表
        articleList()
        
    } catch (error) {
        console.log('取消删除')
    }
}

// 批量删除文章
const batchDeleteArticles = async () => {
    if (selectedIds.value.length === 0) {
        ElMessage.warning('请至少选择一篇文章')
        return
    }
    
    try {
        await ElMessageBox.confirm('确定要删除选中的文章吗？', '确认删除', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
        
        // 调用批量删除接口
        const result = await articleDeleteBatchService(selectedIds.value)
        ElMessage.success('删除成功')
        
        // 刷新列表
        articleList()
        
    } catch (error) {
        console.log('取消删除')
    }
}

// 编辑文章
const editArticle = async (row) => {
    try {
        // 如果是编辑现有文章，需要从后端获取完整信息
        if (row.id) {
            // 获取完整文章信息
            const articleDetail = await articleDetailService(row.id);
            articleModel.value = { ...articleDetail.data };
        } else {
            // 将选中的文章数据填充到表单
            articleModel.value = { ...row }
        }
    } catch (error) {
        // 如果获取详情失败，使用列表中的数据
        articleModel.value = { ...row };
        console.error('获取文章详情失败:', error);
    }
    
    // 显示抽屉
    visibleDrawer.value = true
}

// 添加新文章（重置表单）
const addNewArticle = () => {
    // 重置表单数据
    articleModel.value = {
        title: '',
        categoryId: '',
        coverImg: '',
        content: '',
        state: '',
        id: undefined // 确保id为undefined，以区分添加和编辑
    }
    
    // 显示抽屉
    visibleDrawer.value = true
}
</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章管理</span>
                <div class="extra">
                    <el-button type="primary" @click="addNewArticle">添加文章</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="文章分类：">
                <el-select placeholder="请选择" v-model="categoryId">
                    <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="发布状态：">
                <el-select placeholder="请选择" v-model="state">
                    <el-option label="已发布" value="已发布"></el-option>
                    <el-option label="草稿" value="草稿"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="articleList">搜索</el-button>
                <el-button @click="categoryId = ''; state = ''">重置</el-button>
            </el-form-item>
            <el-form-item>
                <el-button v-if="selectedIds.length > 0" type="danger" @click="batchDeleteArticles">批量删除({{ selectedIds.length }})</el-button>
            </el-form-item>
        </el-form>
        <!-- 文章列表 -->
        <el-table :data="articles" style="width: 100%" @selection-change="handleSelectionChange" v-loading="loading">
            <el-table-column type="selection" width="50"></el-table-column>
            <el-table-column label="文章标题" width="400" prop="title"></el-table-column>
            <el-table-column label="分类" prop="categoryName"></el-table-column>
            <el-table-column label="发表时间" prop="createTime"> </el-table-column>
            <el-table-column label="状态" prop="state"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="editArticle(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteArticle(row.id)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
        <!-- 分页条 -->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3, 5, 10, 15]"
            layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />

        <!-- 抽屉 -->
        <el-drawer v-model="visibleDrawer" title="添加文章" direction="rtl" size="50%">
            <!-- 添加文章表单 -->
            <el-form :model="articleModel" label-width="100px">
                <el-form-item label="文章标题">
                    <el-input v-model="articleModel.title" placeholder="请输入标题"></el-input>
                </el-form-item>
                <el-form-item label="文章分类">
                    <el-select placeholder="请选择" v-model="articleModel.categoryId">
                        <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="文章封面">

                    <!-- 
                        auto-upload:设置是否自动上传
                        action:设置服务器接口路径
                        name:设置上传的文件字段名
                        headers:设置上传的请求头
                        on-success:设置上传成功的回调函数
                     -->
                   
                    <el-upload class="avatar-uploader" :auto-upload="true" :show-file-list="false"
                    action="/api/upload"
                    name="file"
                    :headers="{'Authorization':tokenStore.token}"
                    :on-success="uploadSuccess"
                    >
                        <img v-if="articleModel.coverImg" :src="articleModel.coverImg" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon">
                            <Plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
                <el-form-item label="文章内容">
                    <div class="editor">
                        <quill-editor theme="snow" v-model:content="articleModel.content" contentType="html">
                        </quill-editor>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitArticle('已发布')">{{ articleModel.id ? '更新' : '发布' }}</el-button>
                    <el-button type="info" @click="submitArticle('草稿')">{{ articleModel.id ? '更新草稿' : '草稿' }}</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>
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

/* 抽屉样式 */
.avatar-uploader {
    :deep() {
        .avatar {
            width: 178px;
            height: 178px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 178px;
            height: 178px;
            text-align: center;
        }
    }
}

.editor {
    width: 100%;

    :deep(.ql-editor) {
        min-height: 200px;
    }
}
</style>
package com.ningmeng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.netflix.discovery.converters.Auto;
import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.domain.cms.response.CmsPostPageResult;
import com.ningmeng.framework.domain.course.*;
import com.ningmeng.framework.domain.course.ext.CategoryNode;
import com.ningmeng.framework.domain.course.ext.CourseInfo;
import com.ningmeng.framework.domain.course.response.CourseCode;
import com.ningmeng.framework.domain.course.response.CoursePublishResult;
import com.ningmeng.framework.domain.course.response.CourseView;
import com.ningmeng.framework.domain.course.ext.TeachplanNode;
import com.ningmeng.framework.domain.course.response.AddCourseResult;
import com.ningmeng.framework.exception.CustomExceptionCast;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.QueryResult;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_course.client.CmsPageClient;
import com.ningmeng.manage_course.dao.*;
import javafx.scene.chart.PieChart;
import jdk.nashorn.internal.runtime.options.Option;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private TeachplanMapper teachplanMapper;
    @Autowired
    private TeachplanRepository repository;
    @Autowired
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CategoryMapper mapper;
    @Autowired
    private CourseMarketRepository courseMarketRepository;
    @Autowired
    private CoursePicRepository coursePicRepository;
    @Autowired
    private CoursePubRepository coursePubRepository;

    @Value("${course-publish.dataUrlPre}")
    private String dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String pagePhysicalPath;
    @Value("${course-publish.pageWebPath}")
    private String pageWebPath;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;
    @Value("${course-publish.templateId}")
    private String templateId;
    @Value("${course-publish.siteId}")
    private String siteId;

    @Autowired
    private CmsPageClient cmsPageClient;

    public CoursePub saveCoursePub(String courseId,CoursePub coursePub){
        CoursePub coursePubNew = null;
        Optional<CoursePub> optional = coursePubRepository.findById(courseId);
        if (optional.isPresent()){
            coursePubNew = optional.get();
        }else{
            coursePubNew = new CoursePub();
        }
        BeanUtils.copyProperties(coursePub,coursePubNew);
        coursePubNew.setId(courseId);
        coursePub.setTimestamp(new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        coursePub.setPubTime(date);
        coursePubRepository.save(coursePub);
        return coursePub;
    }


    //创建coursePub对象
    public CoursePub createCoursePub(String courseId){
        CoursePub coursePub = new CoursePub();
        //先同步id
        coursePub.setId(courseId);


        //课程基本信息
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (optional.isPresent()){
            //从基本信息中得到字段然后同步到pub表中；
            BeanUtils.copyProperties(optional.get(),coursePub);
        }

        //查询课程图片
        Optional<CoursePic> optional1 = coursePicRepository.findById(courseId);
        if (optional.isPresent()){
            //从基本信息中得到字段然后同步到pub表中；
            BeanUtils.copyProperties(optional.get(),coursePub);
        }

        Optional<CourseMarket> marketOptional = courseMarketRepository.findById(courseId);
        if (marketOptional.isPresent()){
            CourseMarket courseMarket = marketOptional.get();
            BeanUtils.copyProperties(courseMarket,coursePub);
        }
        TeachplanNode teachplanNode = teachplanMapper.findTeachplanList(courseId);
        String teachplanString = JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(teachplanString);
        return coursePub;


    }





    public CourseBase findCourseBaseById(String courseId){
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseId);
        if(courseBaseOptional.isPresent()){
            CourseBase courseBase = courseBaseOptional.get();
            return courseBase;
        }
        CustomExceptionCast.cast(CommonCode.FAIL);
        return null;
    }
    private CmsPage publish_to(String courseId){

        CourseBase one = this.findCourseBaseById(courseId);
        //发布课程预览页面
        CmsPage cmsPage = new CmsPage();
        //课程站点id 课程站点id，模板，页面名称，页面别名，页面访问路径，数据url
        cmsPage.setSiteId(siteId);
        //模板
        cmsPage.setTemplateId(templateId);
        //页面名称
        cmsPage.setPageName(courseId+".html");
        //页面别名
        cmsPage.setPageAliase(one.getName());
        //页面访问路径
        cmsPage.setPageWebPath(pageWebPath);
        //数据url
        cmsPage.setDataUrl(dataUrlPre+courseId);
        return cmsPage;
    }



    public CoursePublishResult publish(String id){
        CmsPage cmsPage = this.publish_to(id);
        CourseBase one = this.findCourseBaseById(id);
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if (!cmsPostPageResult.isSuccess()){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }
        this.saveCoursePubState(id);


        CoursePub coursePub = createCoursePub(id);
        this.saveCoursePub(id,coursePub);

        return new CoursePublishResult(CommonCode.SUCCESS,cmsPostPageResult.getPageUrl());


    }

    private CourseBase saveCoursePubState(String courseId) {
        CourseBase courseBase = this.findCourseBaseById(courseId);
        courseBase.setStatus("202002");
        CourseBase save = courseBaseRepository.save(courseBase);
        return save;
    }

    public CoursePublishResult preview(String courseId){
        CourseBase one = this.findCourseBaseById(courseId);
        //发布课程预览页面
        CmsPage cmsPage = new CmsPage();
        //课程站点id 课程站点id，模板，页面名称，页面别名，页面访问路径，数据url
        cmsPage.setSiteId(siteId);
        //模板
        cmsPage.setTemplateId(templateId);
        //页面名称
        cmsPage.setPageName(courseId+".html");
        //页面别名
        cmsPage.setPageAliase(one.getName());
        //页面访问路径
        cmsPage.setPageWebPath(pageWebPath);
        //数据url
        cmsPage.setDataUrl(dataUrlPre);
        //远程请求feign cms保存页面信息
        ResponseResult responseResult = cmsPageClient.add(cmsPage);
        if(!responseResult.isSuccess()){
            System.err.println("------------添加失败");
            return new CoursePublishResult(CommonCode.FAIL,null);
        }
        String pageObject = responseResult.getMessage();
        CmsPage cmsPage1 = JSON.parseObject(pageObject, CmsPage.class);
        String pageId = cmsPage1.getPageId();
        if(pageId==null){
            System.err.println("------------pageId为空");
            return new CoursePublishResult(CommonCode.FAIL,null);
        }
        String pageUrl=previewUrl+pageId;
        return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
    }

    /**
     * 根据公司id查询课程
     * @param companyId
     * @return
     */
    public QueryResponseResult findCourseListPage(int pageNo,int pageSize,String companyId){
        if(companyId==null || "".equals(companyId)){
            CustomExceptionCast.cast(CommonCode.FAIL);
        }
        PageHelper.startPage(pageNo,pageSize);
        Page<CourseInfo> pageAll=courseMapper.findCourseListPage(companyId);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(pageAll.getResult());
        queryResult.setTotal(pageAll.getTotal());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }
    public TeachplanNode findTeachplanList(String courseId){
        return teachplanMapper.findTeachplanList(courseId);
    }

    public ResponseResult addTeachplan(Teachplan teachplan) {

        //先去判断课程id和课程计划名称是否为空  如果为空直接报错
        if(teachplan==null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())){
            CustomExceptionCast.cast(CommonCode.FAIL);
        }
        //不为空的话 获取两者
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();
        //判断是否选择父节点，如果没有选，代表根节点 创建节点
        if(StringUtils.isEmpty(parentid)){
            parentid=getTeachplanRoot(courseid);
        }
        //如果有，取出父节点，对象的信息
        Optional<Teachplan> teachplanOptiona = repository.findById(parentid);
        if(!teachplanOptiona.isPresent()){
            CustomExceptionCast.cast(CommonCode.FAIL);
        }
        Teachplan teachplan1 = teachplanOptiona.get();
        //取出级别 来决定我是第几级别
        String grade = teachplan1.getGrade();
        System.err.println("-------------------------"+grade);
        teachplan.setParentid(parentid);
        teachplan.setStatus("0");
        if(grade.equals("1")){
            teachplan.setGrade("2");
        }else if(grade.equals("2")){
            teachplan.setGrade("3");
        }
        teachplan.setCourseid(teachplan1.getCourseid());
        repository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    private String getTeachplanRoot(String courseid) {
        //判断课程id
        Optional<CourseBase> optional = courseBaseRepository.findById(courseid);
        if(!optional.isPresent()){
            return null;
        }
        CourseBase courseBase = optional.get();
        //判断是否当前的这个课程 是否有相同的父节点信息，如果没有，就添加一个根节点（最大的一级），如果有父级，直接返回父级的id
        List<Teachplan> teachplanList = repository.findAllByAndCourseidAndAndParentid(courseid, "0");
        if(teachplanList==null || teachplanList.size()==0){
            Teachplan teachplanRoot = new Teachplan();
            teachplanRoot.setCourseid(courseid);
            teachplanRoot.setPname(courseBase.getName());
            teachplanRoot.setParentid("0");
            teachplanRoot.setGrade("1");
            teachplanRoot.setStatus("0");
            repository.save(teachplanRoot);
            return teachplanRoot.getId();
        }
        Teachplan teachplan = teachplanList.get(0);
        return teachplan.getId();
    }


    public CategoryNode findList() {
        return mapper.selectList();
    }
    @Transactional
    public AddCourseResult addCourseBase(CourseBase courseBase) {
        courseBase.setStatus("202001");
        courseBaseRepository.save(courseBase);
    return new AddCourseResult(CommonCode.SUCCESS, courseBase.getId());
    }
    public CourseBase getCoursebaseById(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Transactional
    public ResponseResult updateCoursebase(String id, CourseBase courseBase) {
        CourseBase one = this.getCoursebaseById(id);
        if(one == null){
            CustomExceptionCast.cast(CommonCode.FAIL);
        }
//修改课程信息
        one.setName(courseBase.getName());
        one.setMt(courseBase.getMt());
        one.setSt(courseBase.getSt());
        one.setGrade(courseBase.getGrade());
        one.setStudymodel(courseBase.getStudymodel());
        one.setUsers(courseBase.getUsers());
        one.setDescription(courseBase.getDescription());
        courseBaseRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }
    public CourseMarket getCourseMarketById(String courseId) {
        Optional<CourseMarket> optional = courseMarketRepository.findById(courseId);
        if(!optional.isPresent()){
        return optional.get();
    }
        return null;
    }
    @Transactional
    public CourseMarket updateCourseMarket(String id, CourseMarket courseMarket) {
        CourseMarket one = this.getCourseMarketById(id);
        if(one!=null){ one.setCharge(courseMarket.getCharge());
            one.setStartTime(courseMarket.getStartTime());//课程有效期，开始时间
            one.setEndTime(courseMarket.getEndTime());//课程有效期，结束时间
            one.setPrice(courseMarket.getPrice()); one.setQq(courseMarket.getQq()); one.setValid(courseMarket.getValid()); courseMarketRepository.save(one);
        }else{
        //添加课程营销信息
            one = new CourseMarket();
            BeanUtils.copyProperties(courseMarket, one);
        //设置课程id
            one.setId(id);
            courseMarketRepository.save(one);
        }
        return one;
    }


    @Transactional
    public ResponseResult saveCoursePic(String courseId, String pic) {
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        CoursePic coursePic=null;
        if(optional.isPresent()){
            coursePic=optional.get();
        }

        if(coursePic==null){
            coursePic= new CoursePic();
        }else{
            coursePic.setCourseid(courseId);
            coursePic.setPic(pic);
        }
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CoursePic findCoursePic(String courseId) {
        return coursePicRepository.findById(courseId).get();
    }

    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        long result=coursePicRepository.deleteByCourseid(courseId);
        if (result==0){
            return new ResponseResult(CommonCode.FAIL);
        }else{
            return new ResponseResult(CommonCode.SUCCESS);
        }

    }

    public CourseView getcourseview(String id) {
        CourseView courseView = new CourseView();
        //获取课程基本信息
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(id);
        if(courseBaseOptional.isPresent()){
            CourseBase courseBase = courseBaseOptional.get();
            courseView.setCourseBase(courseBase);
        }
        //查询课程营销信息
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(id);
        if(courseMarketOptional.isPresent()){
            CourseMarket courseMarket = courseMarketOptional.get();
            courseView.setCourseMarket(courseMarket);
        }
        //查询课程图片信息
        Optional<CoursePic> coursePicOptional = coursePicRepository.findById(id);
        if(coursePicOptional.isPresent()){
            CoursePic coursePic = coursePicOptional.get();
            courseView.setCoursePic(coursePic);
        }
        //查询课程计划信息
        TeachplanNode teachplanList = teachplanMapper.findTeachplanList(id);
        courseView.setTeachplanNode(teachplanList);
        return courseView;
    }
}

package com.itao.search.service.impl;

import com.itao.common.result.ActionResult;
import com.itao.common.utils.ExceptionUtil;
import com.itao.common.utils.JsonUtils;
import com.itao.search.shard.SolrSearchAPI;
import com.itao.search.shard.dto.SolrSearchRequestDTO;
import com.itao.search.shard.dto.SolrSearchResult;
import com.itao.search.solr.SolrSearchDao;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("solrSearchService")
public class SolrSearchServiceImpl implements SolrSearchAPI {

    private static final Logger logger = LoggerFactory.getLogger(SolrSearchServiceImpl.class);

    @Autowired
    private SolrSearchDao solrSearchDao;

    @Override
    public ActionResult<SolrSearchResult> search(SolrSearchRequestDTO solrSearchRequestDTO)  {
        logger.info("查询内容列表开始:{}", JsonUtils.objectToJson(solrSearchRequestDTO));
        ActionResult<SolrSearchResult> actionResult = new ActionResult<>(Boolean.TRUE, "操作成功");
        try {
            // 参数校验
            //TODO
            String queryString = solrSearchRequestDTO.getQueryString();
            int page = solrSearchRequestDTO.getPage();
            int rows = solrSearchRequestDTO.getRows();
            // 创建查询对象
            SolrQuery query = new SolrQuery();
            // 设置查询条件
            query.setQuery(queryString);
            // 设置分页
            query.setStart((page - 1) * rows);
            query.setRows(rows);
            // 设置默认搜素域
            query.set("df", "item_keywords");
            // 设置高亮显示
            query.setHighlight(true);
            query.addHighlightField("item_title");
            query.setHighlightSimplePre("<em style=\"color:red\">");
            query.setHighlightSimplePost("</em>");
            // 执行查询
            SolrSearchResult searchResult = solrSearchDao.search(query);
            // 计算查询结果总页数
            long recordCount = searchResult.getRecordCount();
            long pageCount = recordCount / rows;
            if (recordCount % rows > 0) {
                pageCount++;
            }
            searchResult.setPageCount(pageCount);
            searchResult.setCurPage(page);

            actionResult.setData(searchResult);
        } catch (Exception e) {
            logger.info("查询失败：异常=", e);
            actionResult.setResult(Boolean.FALSE);
            actionResult.setErrorMsg(ExceptionUtil.getStackTrace(e));
        }
        return actionResult;
    }
}

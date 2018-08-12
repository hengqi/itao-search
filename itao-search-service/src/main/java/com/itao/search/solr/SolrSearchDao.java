package com.itao.search.solr;

import com.itao.search.shard.dto.ItaoItemDOExt;
import com.itao.search.shard.dto.SolrSearchResult;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SolrSearchDao {

    @Autowired
    private SolrClient solrClient;


    public SolrSearchResult search(SolrQuery query) throws Exception {
        // 返回值对象
        SolrSearchResult result = new SolrSearchResult();
        // 根据查询条件查询索引库
        QueryResponse queryResponse = solrClient.query(query);
        // 取查询结果
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        // 取查询结果总数量
        result.setRecordCount(solrDocumentList.getNumFound());
        // 商品列表
        List<ItaoItemDOExt> itemList = new ArrayList<>();
        // 取高亮显示
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        // 取商品列表
        for (SolrDocument solrDocument : solrDocumentList) {
            // 创建一商品对象
            ItaoItemDOExt itemExt = new ItaoItemDOExt();
            itemExt.setId((String) solrDocument.get("id"));
            // 取高亮显示的结果
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String title = "";
            if (list != null && list.size() > 0) {
                title = list.get(0);
            } else {
                title = (String) solrDocument.get("item_title");
            }
            itemExt.setTitle(title);
            itemExt.setImage((String) solrDocument.get("item_image"));
            itemExt.setPrice((long) solrDocument.get("item_price"));
            itemExt.setSell_point((String) solrDocument.get("item_sell_point"));
            itemExt.setCategory_name((String) solrDocument.get("item_category_name"));
            // 添加的商品列表
            itemList.add(itemExt);
        }
        result.setItaoItemDOExtList(itemList);
        return result;
    }

}

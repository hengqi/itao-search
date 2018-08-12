package com.itao.search.service.impl;

import com.itao.common.result.ITaoResult;
import com.itao.common.utils.ExceptionUtil;
import com.itao.search.mapper.ItemMapperExt;
import com.itao.search.service.ItemImportService;
import com.itao.search.shard.dto.ItaoItemDOExt;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemImportServiceImpl implements ItemImportService {


    @Autowired
    private ItemMapperExt itemMapperExt;

    @Autowired
    private SolrClient solrClient;

    @Override
    public ITaoResult importAllItems() {
        try {
            // 查询商品列表
            List<ItaoItemDOExt> list = itemMapperExt.getItemExtList();
            // 把商品信息写入索引库
            for (ItaoItemDOExt itemExt : list) {
                // 创建一个SolrInputDocument对象
                SolrInputDocument document = new SolrInputDocument();
                document.setField("id", itemExt.getId());
                document.setField("item_title", itemExt.getTitle());
                document.setField("item_sell_point", itemExt.getSell_point());
                document.setField("item_price", itemExt.getPrice());
                document.setField("item_image", itemExt.getImage());
                document.setField("item_category_name", itemExt.getCategory_name());
                document.setField("item_desc", itemExt.getItem_des());
                // 写入索引库
                solrClient.add(document);
            }
            // 提交修改
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return ITaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
        return ITaoResult.ok();
    }
}

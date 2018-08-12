package com.itao.search.controller;

import com.itao.common.result.ITaoResult;
import com.itao.search.service.ItemImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/manager")
public class ItemExtController {

	@Autowired
	private ItemImportService itemImportService;

	/**
	 * 导入商品数据到索引库
	 */
	@RequestMapping("/importall")
	@ResponseBody
	public ITaoResult importAllItems() {
		ITaoResult result = itemImportService.importAllItems();
		return result;
	}
}

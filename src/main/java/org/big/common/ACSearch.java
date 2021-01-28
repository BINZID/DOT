package org.big.common;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
/**
 *<p><b>Aho-Corasick automation</b></p>
 *<p> Aho-Corasick automation</p>
 * @author BINZI
 *<p>Created date: 2019/03/13 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public class ACSearch {
 
	/**
	 * 构建树、设置失败指针、搜索过程
	 * @param keywords
	 */
	public ACSearch(String[] keywords) {
		buildTree(keywords);
		addFailure();
	}

	private TreeNode root;

	// 查找全部的模式串
	public StringSearchResult[] findAll(String text) {
		// 可以找到 转移到下个节点 不能找到在失败指针节点中查找直到为root节点
		ArrayList<StringSearchResult> results = new ArrayList<StringSearchResult>();
		
		int index = 0;
		TreeNode mid = root;
		while (index < text.length()) {
			TreeNode temp = null;
			while (temp == null) {
				temp = mid.getSonNode(text.charAt(index));
				if (mid == root) {
					break;
				}
				if (temp == null) {
					mid = mid.failure;
				}
			}
			// mid为root 再次进入循环 不需要处理 或者 temp不为空找到节点 节点位移
			if (temp != null)
				mid = temp;
			for (String result : mid.getResults()) {
				results.add(new StringSearchResult(result));
			}
			index++;
		}
		return results.toArray(new StringSearchResult[results.size()]);
	}

	private void addFailure() {
		// 设置二层失败指针为根节点 收集三层节点
		// DFA遍历所有节点 设置失败节点 原则: 节点的失败指针在父节点的失败指针的子节点中查找 最大后缀匹配
		ArrayList<TreeNode> mid = new ArrayList<TreeNode>();// 过程容器
		for (TreeNode node : root.getSonsNode()) {
			node.failure = root;
			for (TreeNode treeNode : node.getSonsNode()) {
				mid.add(treeNode);
			}
		}

		// 广度遍历所有节点设置失败指针 1.存在失败指针 2.不存在到root结束
		while (mid.size() > 0) {
			ArrayList<TreeNode> temp = new ArrayList<TreeNode>();// 子节点收集器
			for (TreeNode node : mid) {
				TreeNode r = node.getParent().failure;
				while (r != null && !r.containNode(node.getChar())) {
					r = r.failure;	// 没有找到,保证最大后缀 (最后一个节点字符相同)
				}
				if (r == null) {	// 是根结
					node.failure = root;
				} else {
					node.failure = r.getSonNode(node.getChar());
					// 重叠后缀的包含
					for (String result : node.failure.getResults()) {
						node.addResult(result);
					}
				}
				// 收集子节点
				for (TreeNode treeNode : node.getSonsNode()) {
					temp.add(treeNode);
				}
			}
			mid = temp;
		}
		root.failure = root;
	}

	private void buildTree(String[] keywords) {
		root = new TreeNode(null, ' ');
		// 判断节点是否存在 存在转移 不存在添加
		for (String word : keywords) {
			TreeNode temp = root;
			for (char ch : word.toCharArray()) {
				if (temp.containNode(ch)) {
					temp = temp.getSonNode(ch);
				} else {
					TreeNode newNode = new TreeNode(temp, ch);
					temp.addSonNode(newNode);
					temp = newNode;
				}
			}
			temp.addResult(word);
		}
	}

	class TreeNode {
		private TreeNode parent;
		private TreeNode failure;
		private char ch;
		private ArrayList<String> results;
		private Hashtable<Character, TreeNode> sonsHash;
		private TreeNode[] sonsNode;
		public TreeNode(TreeNode parent, char ch) {
			this.parent = parent;
			this.ch = ch;
			results = new ArrayList<String>();
			sonsHash = new Hashtable<Character, TreeNode>();
			sonsNode = new TreeNode[] {};
		}
		// 添加子节点
		public void addSonNode(TreeNode node) {
			sonsHash.put(node.ch, node);
			sonsNode = new TreeNode[sonsHash.size()];
			Iterator<TreeNode> iterator = sonsHash.values().iterator();
			for (int i = 0; i < sonsNode.length; i++) {
				if (iterator.hasNext()) {
					sonsNode[i] = iterator.next();
				}
			}
		}
		// 获取子节点中指定字符节点
		public TreeNode getSonNode(char ch) {
			return sonsHash.get(ch);
		}
		// 判断子节点中是否存在该字符
		public boolean containNode(char ch) {
			return getSonNode(ch) != null;
		}
		// 添加一个结果到结果字符中
		public void addResult(String result) {
			if (!results.contains(result))
				results.add(result);
		}
		// 获取字符
		public char getChar() {
			return ch;
		}
		// 获取父节点
		public TreeNode getParent() {
			return parent;
		}
		// 设置失败指针并且返回
		public TreeNode setFailure(TreeNode failure) {
			this.failure = failure;
			return this.failure;
		}
		// 获取所有的孩子节点
		public TreeNode[] getSonsNode() {
			return sonsNode;
		}
		// 获取搜索的字符串
		public ArrayList<String> getResults() {
			return results;
		}
	}
}

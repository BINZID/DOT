package org.big.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.big.common.BuildEntity;
import org.big.common.QueryTool;
import org.big.common.UUIDUtils;
import org.big.entity.Team;
import org.big.entity.UserDetail;
import org.big.entity.UserTeam;
import org.big.repository.TeamRepository;
import org.big.repository.UserTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p><b>Team的Service类</b></p>
 * <p> Team的Service类，与Team有关的业务逻辑方法</p>
 *
 * @author WangTianshan (王天山)
 *         <p>Created date: 2017/9/6 21:35</p>
 *         <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserTeamRepository userTeamRepository;

    @Override
    public List<Team> selectTeamsByUserId(String uid) {
        return this.teamRepository.selectTeamsByUserId(uid);
    }

    @Override
    @Transactional
    public JSON findbyInfo(HttpServletRequest request) {
        String searchText = request.getParameter("search");
        if (searchText == null || searchText.length() <= 0) {
            searchText = "";
        }
        int limit_serch = Integer.parseInt(request.getParameter("limit"));
        int offset_serch = Integer.parseInt(request.getParameter("offset"));
        String sort = "adddate";
        String order = "desc";
       
        JSONObject thisTable = new JSONObject();
        JSONArray rows = new JSONArray();
        List<Team> thisList = new ArrayList<>();
        Page<Team> thisPage = this.teamRepository.searchInfo(searchText, QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
        thisTable.put("total", thisPage.getTotalElements());
        thisList = thisPage.getContent();
        String thisSelect = "";
        String thisEdit = "";
        for (int i = 0; i < thisList.size(); i++) {
            JSONObject row = new JSONObject();
            if (!thisPage.getContent().get(i).getNote().equals("Default")) {
                thisSelect = "<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
                thisEdit =
                        "<a class=\"wts-table-edit-icon\" onclick=\"editThisObject('" + thisList.get(i).getId() + "','team')\" >" +
                                "<span class=\"glyphicon glyphicon-edit\"></span>" +
                                "</a> &nbsp;&nbsp;&nbsp;" +
                                "<a class=\"wts-table-edit-icon\" onclick=\"removeThisObject('" + thisList.get(i).getId() + "','team')\" >" +
                                "<span class=\"glyphicon glyphicon-remove\"></span>" +
                                "</a>";
            }
            row.put("select", thisSelect);
            row.put("name", "<a target='_blank' href='console/team/details/" + thisList.get(i).getId() + "'>" + thisList.get(i).getName() + "</a>");
            row.put("leader", thisList.get(i).getLeader());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String addTime = "";
            try {
                addTime = formatter.format(thisList.get(i).getAdddate());
            } catch (Exception e) {
                //e.printStackTrace();
            }
            row.put("adddate", addTime);
            row.put("edit", thisEdit);
            rows.add(i, row);
        }
        thisTable.put("rows", rows);
        return thisTable;
    }

    @Override
    @Transactional
    public JSON findbyUser(HttpServletRequest request) {
        UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JSON json = null;
        String searchText = request.getParameter("search");
        if (searchText == null || searchText.length() <= 0) {
            searchText = "";
        }
        int limit_serch = Integer.parseInt(request.getParameter("limit"));
        int offset_serch = Integer.parseInt(request.getParameter("offset"));
        String sort = "adddate";
		String order = "desc";
		
        JSONObject thisTable = new JSONObject();
        JSONArray rows = new JSONArray();

        Page<Object> thisPage = this.teamRepository.searchInfoByUser(searchText, thisUser.getId(), QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
        List<Object> thisList = new ArrayList<>();

        thisList = thisPage.getContent();
        thisTable.put("total", thisPage.getTotalElements());
        String thisSelect = "";
        String thisEdit = "";
        for (int i = 0; i < thisList.size(); i++) {
            JSONObject row = new JSONObject();
            Team thisTeam = BuildEntity.buildTeam(thisList.get(i));
            thisSelect = "<input type='checkbox' name='checkbox' id='sel_" + thisTeam.getId() + "' />";
            thisEdit = "<a class=\"wts-table-edit-icon\" onclick=\"editThisObject('" + thisTeam.getId() + "','team')\" >" +
                            "<span class=\"glyphicon glyphicon-edit\"></span>" +
                            "</a> &nbsp;&nbsp;&nbsp;" +
                            "<a class=\"wts-table-edit-icon\" onclick=\"removeThisObject('" + thisTeam.getId() + "','team')\" >" +
                            "<span class=\"glyphicon glyphicon-remove\"></span>" +
                            "</a>";
            row.put("select", thisSelect);
            row.put("name", "<a target='_blank' href='console/team/details/" + thisTeam.getId() + "'>" + thisTeam.getName() + "</a>");
            row.put("leader", thisTeam.getLeader());
            row.put("members", "共" + this.teamRepository.countMembersByTeamId(thisTeam.getId()) + "人");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String addTime = "";
            try {
                addTime = formatter.format(thisTeam.getAdddate());
            } catch (Exception e) {
                //e.printStackTrace();
            }
            row.put("adddate", addTime);
            row.put("edit", thisEdit);
            rows.add(i, row);
        }
        thisTable.put("rows", rows);
        json = thisTable;
        return json;
    }

    @Override
    public Team findbyID(String ID) {
        return this.teamRepository.getOne(ID);
    }

    @Override
    public void saveOne(Team thisTeam) {
        if (thisTeam.getId() == null || thisTeam.getId().equals("") || thisTeam.getId().length() <= 0) {
            thisTeam.setId(UUIDUtils.getUUID32());
            thisTeam.setAdddate(new Timestamp(System.currentTimeMillis()));
        }
        this.teamRepository.save(thisTeam);
    }

    @Override
    public void saveOneByUser(Team thisTeam) {
        thisTeam.setId(UUIDUtils.getUUID32());
    	UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	thisTeam.setStatus("1");
        thisTeam.setLeader(thisUser.getId());
        thisTeam.setAdddate(new Timestamp(System.currentTimeMillis()));
        
        UserTeam thisUserTeam = new UserTeam();
        thisUserTeam.setUserId(thisUser.getId());
        thisUserTeam.setTeamId(thisTeam.getId());

        this.teamRepository.save(thisTeam);
        this.userTeamRepository.save(thisUserTeam);
    }

    @Override
    public Boolean removeOne(String ID) {
    	return removeOneOrMoreTeamByTid(ID);
    }

    @Override
    public Boolean removeOneByUser(String ID) {
    	return removeOneOrMoreTeamByTid(ID);
    }

    /**
     * 通过TeamId实现单个删除或批量删除
     * @param ID
     */
	private Boolean removeOneOrMoreTeamByTid(String ID) {
		Team thisTeam = this.teamRepository.findOneById(ID);
    	UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String mark = this.teamRepository.findOneById(ID).getMark();
    	if (!mark.equals("Default") && thisUser.getId().equals(thisTeam.getLeader())) {
    		this.userTeamRepository.deleteByTeamId(ID);    // 先操作中间表，删除外键关系
    		thisTeam.setStatus("0");
    		this.teamRepository.save(thisTeam);            // 再操作Team表
    		return true;
		}
    	return false;
	}

    @Override
    public Team findOneByName(String name) {
        return this.teamRepository.findOneByName(name);
    }

    @Override
    public int countMembersByTeamId(String ID) {
        return this.teamRepository.countMembersByTeamId(ID);
    }

    @Override
    public boolean removeMembersByTeamIdAndUserId(HttpServletRequest request) {
    	String teamId=request.getParameter("teamId");
        String userId=request.getParameter("userId");
    	try {
			Team thisTeam = this.teamRepository.findOneById(teamId);
			if (!userId.equals(thisTeam.getLeader())) {
				this.userTeamRepository.deleteMembersByTeamIdAndUserId(teamId, userId);
				return true;
			}
		} catch (Exception e) {
		}
    	return false;
    }

    @Override
    public void updateTeamInfoByLeader(HttpServletRequest request) {
    	String teamId=request.getParameter("teamId");
        String userId=request.getParameter("userId");
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(teamId)) {
        	// 修改Team表的name & leader字段
        	this.userTeamRepository.updateTeamInfoByLeader(userId, teamId);
		}
    }

	@Override
	public JSON newOne(Team thisTeam, HttpServletRequest request) {
		JSONObject thisResult = new JSONObject();
		try {
			thisTeam.setId(UUIDUtils.getUUID32());
			// 获取当前登录用户
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			thisTeam.setLeader(thisUser.getId());
			thisTeam.setAdddate(new Timestamp(System.currentTimeMillis()));
			thisTeam.setStatus("1");
			thisTeam.setBgurl("/img/dataset-bg.jpg");
			
			UserTeam thisUserTeam = new UserTeam();
	        thisUserTeam.setUserId(thisUser.getId());
	        thisUserTeam.setTeamId(thisTeam.getId());
			
			this.teamRepository.save(thisTeam);
			this.userTeamRepository.save(thisUserTeam);
			
			thisResult.put("result", true);
			thisResult.put("newId", thisTeam.getId());
		} catch (Exception e) {
			thisResult.put("result", false);
		}
		return thisResult;
	}

	@Override
	public void saveForUpdate(Team thisTeam) {
		this.teamRepository.save(thisTeam);
	}

    @Override
    public JSON findMyTeamsByUserId(String userId, HttpServletRequest request) {
        int limit_serch = Integer.parseInt(request.getParameter("limit"));		// 1.limit 一次查询返回的个数，默认值10
        int offset_serch = Integer.parseInt(request.getParameter("offset"));	// 2.offset从第几个开始查，默认值0
    	String sort = "adddate";
		String order = "desc";
		
        JSONObject thisSelect = new JSONObject();
        String searchText = "";
        Page<Team> thisPage = this.teamRepository.findMyTeamsByUserId(userId, searchText, QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
        List<Team> thisList = new ArrayList<>();
        thisList = thisPage.getContent();
        thisSelect.put("total", thisPage.getTotalElements());		// NO1：总数
        thisSelect.put("page", offset_serch);						// NO2：offset
        thisSelect.put("rows", thisList);							// NO3：Taxon下的所有Team
        return thisSelect;
    }
    
    @Override
    public JSON findMyJoinTeamsByUserId(String userId, HttpServletRequest request) {
        int limit_serch = Integer.parseInt(request.getParameter("limit"));		// 1.limit 一次查询返回的个数，默认值10
        int offset_serch = Integer.parseInt(request.getParameter("offset"));	// 2.offset从第几个开始查，默认值0
    	String sort = "adddate";
		String order = "desc";
		
        JSONObject thisSelect = new JSONObject();
        String searchText = "";
        this.teamRepository.findJoinTeamsByUserId(userId, searchText, QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
        Page<Team> thisPage = this.teamRepository.findJoinTeamsByUserId(userId, searchText, QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
        List<Team> thisList = new ArrayList<>();
        thisList = thisPage.getContent();
        thisSelect.put("total", thisPage.getTotalElements());		// NO1：总数
        thisSelect.put("page", offset_serch);						// NO2：offset
        thisSelect.put("rows", thisList);							// NO3：Taxon下的所有Team
        return thisSelect;
    }

	@Override
	public void handleTeamSession(String id, HttpServletRequest request) {
		request.getSession().setAttribute("teamId", id);	// 将TeamId存放到Session中
        request.getSession().setAttribute("teamName", this.teamRepository.findOneById(id).getName());
        request.getSession().setAttribute("team", this.teamRepository.findOneById(id));
        //删除dataset中的记录
        request.getSession().removeAttribute("datasetID");
        request.getSession().removeAttribute("datasetName");
        request.getSession().removeAttribute("dataset");
	}

	@Override
	public Boolean countTeamsByName(HttpServletRequest request) {
		String name = request.getParameter("name");
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long num = 0;
		if (StringUtils.isNotBlank(name)) {
			num = this.teamRepository.countTeamsByName(name, thisUser.getId());
		}
		if (num > 0) {
			return false;
		}else {
			return true;
		}
	}

}
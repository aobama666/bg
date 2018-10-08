package com.sgcc.bg.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import com.sgcc.bg.common.RPCDataQuery;

/**
 * RPC 数据查询客户端 工具类
 * 
 * @author epri-xpjt
 *
 */
public class RPCDataClient {

	/**
	 * 启动RPC数据查询调用
	 * 
	 * @param sql
	 *            sql语句 预编译的SQL语句(带问号)
	 * @param paramMap
	 *            参数 查询参数(不可以有null) map中key代表占位符序列 value代表值
	 * @return List<Map<String,String>> 查询结果信息
	 * @throws InterruptedException
	 */
	public static List<Map<String, String>> startClient(String sql, Map<Integer, String> paramMap)
			throws InterruptedException {
		//从数据库中获取配置数据 IP 端口 超时时间
		String IP = ConfigUtils.getConfig("rpc_query_data_ip");
		int PORT = Integer.parseInt(ConfigUtils.getConfig("rpc_query_data_port"));
		int TIMNEOUT = Integer.parseInt(ConfigUtils.getConfig("rpc_query_data_timeout"));
		TSocket socket = null;
		try {
			socket = new TSocket(IP, PORT, TIMNEOUT);
			TProtocol portol = new TBinaryProtocol(socket);
			RPCDataQuery.Client client = new RPCDataQuery.Client(portol);
			socket.open();
			List<Map<String, String>> result = client.query(sql, paramMap);
			return result;
		} catch (TTransportException e) {
			e.printStackTrace();
			return null;
		} catch (TException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		String sql = "select * from PROJ where POST1 like ?";
		HashMap<Integer, String> msp = new HashMap<Integer, String>();
		msp.put(1, "%大唐安阳中水%");
		System.out.println(startClient(sql, msp));
	}
}

function callBack(actionStack, results, paramArray) {
	var request = actionStack.getHttpRequest();
	var response = actionStack.getHttpResponse();
	if (paramArray.length == 2 && paramArray[1] == "server")
		request.getRequestDispatcher(paramArray[0]).forward(request, response);
	else
		response.sendRedirect(paramArray[0]);
	return results;
}
// ���� 1 ת����ַ������2 ת����ʽ server ��������ת���������ͻ����ض���

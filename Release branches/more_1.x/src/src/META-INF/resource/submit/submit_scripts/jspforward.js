function callBack(actionStack, results, paramArray) {
	var request = actionStack.getRequest();
	var response = actionStack.getResponse();
	if (paramArray.length == 2 && paramArray[1] == "server")
		request.getRequestDispatcher(paramArray[0]).forward(request, response);
	else
		response.sendRedirect(paramArray[0]);
	return results;
}
// ���� 1 ת����ַ������2 ת����ʽ server ��������ת���������ͻ����ض���

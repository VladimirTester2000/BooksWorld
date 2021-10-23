<#-- объявляем переменные-->
<#--проверяем контекст Спринга на налчие-->
<#assign
known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <#assign
    <#--        получим объект User если он авторизован в SpringSecurity (principal - UserDetails)-->
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal.getUser()
    name = user.getUsername()
    isAdmin = user.isAdmin()
    >
<#else >
    <#assign
    <#--            значения заглушки-->
    name = "unknown"
    isAdmin = false
    >
</#if>
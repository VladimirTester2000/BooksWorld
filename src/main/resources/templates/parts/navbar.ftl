<#include "security.ftl">
<#import "loginForm.ftl" as l>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">Books World</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <#if userId gt -1>
                <li class="nav-item">
                    <a class="nav-link" href="/like?user=${userId}">My Likes</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/mySubscriptions">My subscriptions</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/bookList">Book list</a>
                </li>
            </#if>
            <#if isAdmin>
                <li class="nav-item">
                    <a class="nav-link" href="/user">User List</a>
                </li>
            </#if>
            <#if isManager>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button" data-toggle="dropdown" aria-expanded="false">
                        Manager features
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                        <a class="dropdown-item" href="/manager/bookEditor">Book editor</a>
                        <a class="dropdown-item" href="/manager/authorEditor">Author editor</a>
                    </div>
                </li>
            </#if>
        </ul>

        <div class="navbar-nav nav justify-content-end mr-5">
            <#if known>
                <#if user??>
                    <a class="btn btn-light nav-link" href="/user/profile">${name}</a>
                <#else >
                    <div class="navbar-text">${name}</div>
                </#if>
                <form method="post" action="/logout">
                    <button type="submit" class="btn btn-light nav-link">Sign Out</button>
                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                </form>
            <#else >
                <a class="btn btn-light nav-link" href="/login">Log In</a>
                <a class="btn btn-light nav-link" href="/signup">Sign Up</a>
            </#if>
        </div>


    </div>
</nav>
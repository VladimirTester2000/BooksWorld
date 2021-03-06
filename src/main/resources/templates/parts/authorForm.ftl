<#macro authorForm path>
    <form action="${path}" method="post">
        <div class="form-group col-sm-3">
            <label for="exampleFormControlInput1">Имя автора</label>
            <input type="text" name="name"
                   class="form-control ${(nameError??)?string('is-invalid','')}"
                   placeholder="ex: А.С. Пушкин"
                    <#if author??>
                        value="${author.name}"
                    </#if>
            >
            <#if nameError??>
                <div class="invalid-feedback">${nameError}</div>
            </#if>
        </div>
        <div class="form-group col-sm-3">
            <label for="birthday">Дата рождения</label>
            <input type="date" name="birthday"
                   class="form-control ${(birthdayError??)?string('is-invalid','')}"
                    <#if author??>
                        value="${author.birthday?if_exists}"
                    </#if>
            >
            <#if birthdayError??>
                <div class="invalid-feedback">${birthdayError}</div>
            </#if>
        </div>
        <div class="form-group col-sm-3">
            <label for="dateOfDeath">Дата смерти</label>
            <input type="date" name="dateOfDeath"
                   class="form-control ${(dateOfDeathError??)?string('is-invalid','')}"
                    <#if author??>
                        value="${author.dateOfDeath?if_exists}"
                    </#if>
            >
            <#if dateOfDeathError??>
                <div class="invalid-feedback">${dateOfDeathError}</div>
            </#if>
        </div>
        <div class="form-group col-sm-5">
            <label for="biography">Краткая биография</label>
            <textarea class="form-control ${(biographyError??)?string('is-invalid','')}" name="biography" rows="4"><#if author??>${author.biography?if_exists}</#if></textarea>
            <#if biographyError??>
                <div class="invalid-feedback">${biographyError}</div>
            </#if>
        </div>
        <div class="form-group col-sm-3">
            <button type="submit" class="btn btn-primary">Submit</button>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#if author?? && author.authorId??>
            <input type="hidden" name="authorId" value="${author.authorId}"/>
        </#if>
    </form>
</#macro>

<#macro cardBody author>
    <div class="card-body">
        <h5 class="card-title">
            ${author.name}
        </h5>
        <h6 class="card-subtitle mb-2 text-muted">
            (${author.birthday} - ${author.dateOfDeath!"..."})
        </h6>
        <p class="card-text">
            ${author.biography!""}
        </p>
        <#if author.countSubscribers?? && author.countSubscribers gt 0>
            <h6 class="card-subtitle mb-2 text-muted">
                Subscribers: ${author.countSubscribers}
            </h6>
        </#if>
        <#if author.countBook?? && author.countBook gt 0>
            <h6 class="card-subtitle mb-2 text-muted">
                Count publications: ${author.countBook}
            </h6>
        </#if>

        <#if author.isSubscription()>
            <a href="/unsubscribe?author=${author.authorId}" class="btn btn-outline-danger">Unsubscribe</a>
        <#else >
            <a href="/subscribe/${author.authorId}" class="btn btn-outline-success">Subscribe</a>
        </#if>
        <#include "security.ftl">
        <#if isManager>
            <div class="card-footer text-center mt-2">
                <a class="btn btn-info" href="/manager/authorEditor?author=${author.authorId}">Edit ${author.name}</a>
            </div>
        </#if>
    </div>
</#macro>
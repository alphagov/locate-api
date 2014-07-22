<#include "header.ftl">
<article>
        <hr/>
        <p>Free postcode lookup service for the UK public sector</p>

        <h2>What is the Locate API?</h2>

        <p>Locate is a free API service that does postcode lookups against the latest available data from the Ordinance
            Survey. It uses AddressBase data and it is free to use for any public sector organisation in the UK.</p>

        <p>Read more about Locate and how to access the service in the <a href="https://github.com/alphagov/locate-api">technical
            documentation on GitHub.</a></p>

        <p>You’ll need your National Insurance number (if you have one). If you’re living abroad, you may also need your
            passport.</p>

        <hr/>

        <h2>Generate an API token</h2>

        <p>We need a few details in order to generate your very own API token</p>
        <form action="/locate/create-user" method="POST">

        <label for="name">
            Name
        </label>

        <input id="name" name="name" value="" autocomplete="off" class="text first-name long" type="text">

        <label for="email">
            Email
        </label>

        <input id="email" name="email" value="" autocomplete="off" class="text email long" type="text">

        <label for="organisation">
            Organisation
        </label>

        <input id="organisation" name="organisation" value="" autocomplete="off" class="text first-name long"
               type="text">

        <h3>What type of addresses do you want to receive from the API?</h3>
        <fieldset>
        <fieldset class="validate " data-validation-name="query" data-validation-type="fieldset" data-validation-rules="atLeastOneNonEmpty" data-validation-children="residential commercial residential-commercial all">

            <label for="residential_data" class="selectable">
                <input id="residential_data" name="query_type" value="residential" class="radio validate"
                       data-validation-name="residential" data-validation-type="field" data-validation-rules="nonEmpty"
                       type="radio" checked>
                Residential
            </label>

            <label for="commercial_data" class="selectable">
                <input id="commercial_data" name="query_type" value="commercial" class="radio validate"
                       data-validation-name="commercial" data-validation-type="field" data-validation-rules="nonEmpty"
                       type="radio">
                Commercial
            </label>

            <label for="residential-commercial_data" class="selectable">
                <input id="residential-commercial_data" name="query_type" value="residential-commercial"
                       class="radio validate" data-validation-name="residential-commercial" data-validation-type="field"
                       data-validation-rules="nonEmpty" type="radio">
                Residential and commercial
            </label>

            <label for="all_data" class="selectable">
                <input id="all_data" name="query_type" value="all" class="radio validate" data-validation-name="all"
                       data-validation-type="field" data-validation-rules="nonEmpty" type="radio">
                All
            </label>
        </fieldset>
        <h3>Do you want the full data or only the presentation block?</h3>
        <fieldset>
            <label for="full" class="selectable">
                <input id="full" name="data_type" value="full" class="radio" data-validation-name="full"
                       data-validation-type="field" data-validation-rules="nonEmpty" type="radio" checked>
                Full
            </label>

            <label for="presentation" class="selectable">
                <input id="presentation" name="data_type" value="presentation" class="radio"
                       data-validation-name="presentation" data-validation-type="field" data-validation-rules="nonEmpty"
                       type="radio">
                Presentation
            </label>
        </fieldset>
        <hr/>
        <h2>I understand that:</h2>

        <p>By applying for a Locate API token I am confirming I represent a UK public sector organisation.</p>

        <p>Usage of the Locate API is limited to 1000 requests per calendar day</p>

        <div class="warning">Locate API is in 'alpha' stage so there are not guaranteed uptime or service levels</div>

            <input id="continue" class="button next" value="Generate token" type="submit">
        </form>

    </article>
<#include "footer.ftl">

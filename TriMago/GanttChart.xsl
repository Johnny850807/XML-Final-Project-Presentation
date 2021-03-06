<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
        <head>
            <script type="text/javascript" src="date.js"></script>
            <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
            <script type="text/javascript">
                google.charts.load('current', {'packages':['gantt']});
                google.charts.setOnLoadCallback(drawChart);
                
                function daysToMilliseconds(days) {
                    return days * 24 * 60 * 60 * 1000;
                }

                function getDiffMillisecondsFromTwoDate(date1,date2) {
                    var timeDiff = Math.abs(date2.getTime() - date1.getTime());
                    var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
                    return daysToMilliseconds(diffDays);
                }

                function drawChart() {
                    var date_pattern = 'yyyy d NNN.';
                    var data = new google.visualization.DataTable();
                    data.addColumn('string', 'Task ID');
                    data.addColumn('string', 'Task Name');
                    data.addColumn('string', 'Resource');
                    data.addColumn('date', 'Start Date');
                    data.addColumn('date', 'End Date');
                    data.addColumn('number', 'Duration');
                    data.addColumn('number', 'Percent Complete');
                    data.addColumn('string', 'Dependencies');

                    var rows = [];
                    <xsl:apply-templates select="//Task"/>
                    
                    data.addRows(rows);

                    var options = {
                        height:  rows.length * 44
                    };

                    var chart = new google.visualization.Gantt(document.getElementById('chart_div'));

                    chart.draw(data, options);
                }
            </script>
        </head>
        <body>
            <div id="chart_div"></div>
        </body>
    </xsl:template>

    <xsl:template match="Task">
        var startDate = getDateFromFormat('<xsl:value-of select="@StartDate" />',date_pattern);
        var endDate = getDateFromFormat('<xsl:value-of select="@EndDate" />',date_pattern);
        var month = startDate.getUTCMonth() + 1; //months from 1-12
                    var day = startDate.getUTCDate();
                    var year = startDate.getUTCFullYear();
        rows.push(['<xsl:value-of select="@id" />', '<xsl:value-of select="@name" />', '<xsl:value-of select="parent::*/@name" />',
                          startDate  , endDate  , daysToMilliseconds(startDate,endDate) , 100, null]);
    </xsl:template>

</xsl:stylesheet>
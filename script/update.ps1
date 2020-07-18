$criteria = "Type='software' and IsAssigned=1 and IsHidden=0 and IsInstalled=0"
$searcher = (New-Object -COM Microsoft.Update.Session).CreateUpdateSearcher()
$updates = $searcher.Search($criteria).Updates

If ($searchresult.Updates.Count -gt 0)
{
    $count = $searchresult.Updates.Count
    For ($i = 0; $i -lt $Count; $i++) {
        $Update = $searchresult.Updates.Item($i)
        Write-Output $Update.Title
    }
}
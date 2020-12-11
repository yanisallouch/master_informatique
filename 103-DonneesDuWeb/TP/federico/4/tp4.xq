(: (:(:(:(:(:1:)
for $x in //train/@numero return $x [../descendant::bar]
(:2:)
:)
distinct-values(for $resa in //resa/@id
where (for $usager in //usager/@id return $usager) = $resa
return $resa)
(:3:)
:)
sort(distinct-values(
  for $resaNumero in //resa/@numero
  return $resaNumero
))
:)
(:4:)
:)
for $x in /gare/train
  for $y in $x/voiture
  where count($y/resa) > 1 return  $x/@numero :)
(:5:)
for $usager in /gare/usager
  for $voiture in /gare/train/voiture
      where count($voiture/resa[@id = $usager/@id ]) return  $usager/@nom
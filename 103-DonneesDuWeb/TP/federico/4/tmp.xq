for $x in /gare/train
  for $y in $x/voiture
  where count($y/resa) > 1 return  $x/@numero
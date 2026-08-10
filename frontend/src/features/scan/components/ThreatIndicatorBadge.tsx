import { Badge } from "../../../components/ui/badge";

interface Props {
  malicious: boolean;
  suspicious: boolean;
}

export default function ThreatIndicatorBadge({ malicious, suspicious }: Props) {
  if (malicious) {
    return <Badge variant="destructive">Malicious</Badge>;
  }
  if (suspicious) {
    return <Badge variant="warning">Suspicious</Badge>;
  }
  return <Badge variant="success">Clean</Badge>;
}

import { useRef, useState } from "react";
import jsQR from "jsqr";
import { Upload } from "lucide-react";

import { Button } from "../../../components/ui/button";

interface Props {
  onDecoded: (url: string) => void;
}

export default function QrUploadInput({ onDecoded }: Props) {
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [error, setError] = useState<string | null>(null);
  const [fileName, setFileName] = useState<string | null>(null);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    setError(null);
    setFileName(file.name);

    const reader = new FileReader();

    reader.onload = () => {
      const image = new Image();

      image.onload = () => {
        const canvas = document.createElement("canvas");
        canvas.width = image.width;
        canvas.height = image.height;

        const context = canvas.getContext("2d");
        if (!context) {
          setError("Couldn't read this image.");
          return;
        }

        context.drawImage(image, 0, 0);
        const imageData = context.getImageData(0, 0, canvas.width, canvas.height);

        const decoded = jsQR(imageData.data, imageData.width, imageData.height);

        if (decoded?.data) {
          onDecoded(decoded.data);
        } else {
          setError("Couldn't detect a QR code in this image.");
        }
      };

      image.onerror = () => setError("Couldn't read this image.");
      image.src = reader.result as string;
    };

    reader.readAsDataURL(file);
  };

  return (
    <div className="space-y-3">
      <input
        ref={fileInputRef}
        type="file"
        accept="image/*"
        className="hidden"
        onChange={handleFileChange}
      />

      <Button
        type="button"
        variant="outline"
        className="w-full"
        onClick={() => fileInputRef.current?.click()}
      >
        <Upload />
        {fileName ? `Uploaded: ${fileName}` : "Upload a QR code image"}
      </Button>

      {error && (
        <p className="text-sm text-destructive">{error}</p>
      )}
    </div>
  );
}

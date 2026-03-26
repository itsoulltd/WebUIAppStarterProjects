
export interface CsvRow {
    [key: string] : string | number | boolean;
}

/**
 * read(..., header: string[]): if header is empty then parse first-row as headers.
 */
export const Csv = {
    read: <T extends CsvRow>(csv: string | undefined, separator: string = ",", indexed: boolean = true, header: string[] = []): T[] => {
        if (csv === undefined || csv === "") return [];

        // remove BOM if present
        if (csv.charCodeAt(0) === 0xfeff) csv = csv.slice(1);

        // normalize ALL line endings to \n
        const normalized = csv.replace(/\r\n/g, "\n").replace(/\r/g, "\n");

        // split + remove empty lines [split(/\r?\n/) means match \n, optionally preceded by \r]
        const lines = normalized
            .split(/\r?\n/)
            .map(line => line.trim())
            .filter(line => line !== "");

        if (lines.length === 0) return [];

        // extract headers
        const headers = (header.length > 0) ? header : lines[0].split(separator).map(h => h.trim());

        // parse rows
        const data: T[] = lines.slice(1).map((line, rowIndex) => {
            const values = line.split(separator);
            const row: CsvRow = indexed ? {index: rowIndex} : {};
            headers.forEach((header, i) => {
                row[header] = values[i]?.trim() || "";
            });
            return row as T; //not a type-safe casting!
        });
        return data;
    },
    convert: <T extends CsvRow>(rows: T[]): string => {
        if (!rows.length) return "";
        // create headers from keys:
        const headers = Object.keys(rows[0]);
        // convert array of string:
        const csvRows = [
            headers.join(","), // header row
            ...rows.map((row) =>
                headers.map((field) => JSON.stringify(row[field] ?? "")).join(",")
            )
        ];
        // generate csv string:
        return csvRows.join("\n");
    },
    writeToBinary: (csv: string): Blob => {
        // create blob
        const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
        return blob;
    },
    download: (blob: Blob, filename: string) => {
        // create temporary download link:
        const url = URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", filename);
        // augment the link-clicked & initiate browser download:
        document.body.appendChild(link);
        link.click();
        // cleanup:
        document.body.removeChild(link);
        URL.revokeObjectURL(url);
    },
}
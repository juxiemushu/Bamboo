package ${package.Mapper};

import org.apache.ibatis.annotations.Mapper;
import ${package.Entity}.${entity};
import ${superMapperClassPackage};

/**
 * ${table.comment!} Mapper 接口
 *
 * @author ${author}
 * @since ${date}
 */
@Mapper
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

}
</#if>
